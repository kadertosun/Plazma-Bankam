package com.example.plazmabankam2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.plazmabankam2.ui.info.window.CustomInfoWindowGoogleMap;
import com.example.plazmabankam2.ui.login.LoginActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, OnMarkerDragListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private GoogleMap mMap = null;
    private static MapsActivity instance = null;
    private FusedLocationProviderClient mFusedLocationClient;

    private Marker homeMarker = null;
    private FloatingActionButton fabSettings = null;
    private FloatingActionButton fabLogin = null;
    private FloatingActionButton fabEventsList = null;


    private List<Event> allEventsInRadius;
    private List<Event> myEventsInRadius;
    private List<String> eventCategories = null;
    private Set<String> categoriesEnabled = null;
    private Session currentSession = null;
    private User loggedInUser = null;
    private LatLng currentLocation = null;
    private boolean isFABOpen = false;

    private static boolean filtersEnabled = true;
    private static boolean notifications_enabled = false;
    private final static int PERMISSION_ID = 44;
    private final static int mapZoomLevel = 12;
    private final static int minHours = 1;
    private final static int maxHours = 23;
    private int maxRadius; // in meters

    public static MapsActivity getInstance(){return instance; }

    public MapsActivity(){
        instance = this;
        allEventsInRadius = new ArrayList<>();
        myEventsInRadius = new ArrayList<>();
        currentLocation = new LatLng(38.0, 40.0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (eventCategories == null) {
            eventCategories = Arrays.asList(getResources().getStringArray(R.array.category_entries));
        }

        onSharedPreferenceChanged(sharedPreferences, SettingsActivity.KEY_PREF_NOTIFICATION_SWITCH);
        onSharedPreferenceChanged(sharedPreferences, SettingsActivity.KEY_PREF_FILTER_SWITCH);
        onSharedPreferenceChanged(sharedPreferences, SettingsActivity.KEY_PREF_DISTANCE_EDIT_TEXT);
        onSharedPreferenceChanged(sharedPreferences, SettingsActivity.KEY_PREF_CATEGORIES_MULTI_SELECT_LIST);


        fabSettings = findViewById(R.id.fab_settings);
        fabLogin = findViewById(R.id.fab_login);
        fabEventsList = findViewById(R.id.fab_event_list);
        currentSession = new Session(this);

        loggedInUser = currentSession.reloadLoggedInUser();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public  void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!isFABOpen) {
            super.onBackPressed();
        } else {
            showFABMenu(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SettingsActivity.KEY_PREF_NOTIFICATION_SWITCH)) {
            notifications_enabled = sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION_SWITCH, false);
        } else if (key.equals(SettingsActivity.KEY_PREF_FILTER_SWITCH)) {
            filtersEnabled = sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_FILTER_SWITCH,true);
        } else if (key.equals(SettingsActivity.KEY_PREF_DISTANCE_EDIT_TEXT)) {
            maxRadius = Integer.parseInt(sharedPreferences.getString(SettingsActivity.KEY_PREF_DISTANCE_EDIT_TEXT,"10000"));
        } else if (key.equals(SettingsActivity.KEY_PREF_CATEGORIES_MULTI_SELECT_LIST)) {
            categoriesEnabled = sharedPreferences.getStringSet(SettingsActivity.KEY_PREF_CATEGORIES_MULTI_SELECT_LIST, new HashSet<>(eventCategories));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager
                .getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void showFABMenu(boolean show){
        if (show) {
            fabSettings.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
            fabLogin.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
            fabEventsList.animate().translationY(-getResources().getDimension(R.dimen.standard_195));

        } else {

            fabSettings.animate().translationY(0);
            fabLogin.animate().translationY(0);
            fabEventsList.animate().translationY(0);
        }
        isFABOpen = show;
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.fab_user_menu:
                showFABMenu(!isFABOpen);
                break;
            case R.id.fab_settings:
                startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
                break;
            case R.id.fab_login:
                startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                break;
            case R.id.fab_event_list:
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                break;

        }
    }

    public void setLoggedInUser(User newUser) {
        loggedInUser = newUser;
        currentSession.saveLoggedInUser(loggedInUser);
        String welcome = getString(R.string.welcome_user) + loggedInUser.getDisplayName();
        Toast.makeText(this, welcome, Toast.LENGTH_SHORT).show();
    }

    public List<Event> getMyEventsList(){
        return myEventsInRadius;
    }

    public User getLoggedInUser(){
        return loggedInUser;
    }

    public String logoutCurrentUser(){
        String strBye = loggedInUser.getDisplayName() + getString(R.string.bye_user);
        loggedInUser = new User("0", 0, "misafir", "misafir", "Geçiş Yok");
        currentSession.saveLoggedInUser(loggedInUser);
        Toast.makeText(this, strBye, Toast.LENGTH_SHORT).show();
        return loggedInUser.getDisplayName();
    }

    private void setCurrentLocation(Location loc, int id){
        LatLng location = new LatLng(loc.getLatitude(), loc.getLongitude());
        Log.i(getString(R.string.app_name), String.format(getString(R.string.label_updating_current_location), id, location.latitude, location.longitude));
        currentLocation = location;
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, mapZoomLevel));
            homeMarker.setPosition(location);
        }
        refreshEventsList();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(
                        this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    setCurrentLocation(location, 1);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, R.string.prompt_turn_on_location, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        Log.i(getString(R.string.app_name), getString(R.string.label_location_null));
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            setCurrentLocation(location, 2);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLastLocation();

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, mapZoomLevel));

        if (homeMarker == null) {
            homeMarker = mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_marker)));
            if (loggedInUser.getRole() > 0) {
                homeMarker.setDraggable(true);
            }
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) { }

    @Override
    public void onMarkerDrag(Marker marker) { }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // Markers do not update their position after a drag.
        // This is a simple workaround
        homeMarker.setPosition(marker.getPosition());
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (loggedInUser.getRole() > 0) {
            Object tag = marker.getTag();
            Event event = null;
            if (tag instanceof Event) {
                event = (Event) tag;
                if (!event.getUserID().equals(loggedInUser.getUserID())) {
                    return false;
                }
            }
            marker.hideInfoWindow();
            createEventDialogBox(this, event);
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void addEvent(LatLng location, String title, String details, String category, int expires) {
        if (!TextUtils.isEmpty(details)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String eventID = db.collection("events").document().getId();

            Event event = new Event(eventID, loggedInUser.getUserID(), loggedInUser.getDisplayName(), location, title, details, category, expires);

            db.collection("events").document(eventID)
                    .set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Event added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to Add Event!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEvent(Event event, String title, String details, String category, int expires){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dbRef = db.collection("events").document(event.getEventID());

        if (!title.equals(event.getTitle())) {
            dbRef.update("title", title);
        }

        if (!details.equals(event.getDetails())) {
            dbRef.update("details", details);
        }

        if (!category.equals(event.getCategory())) {
            dbRef.update("category", category);
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, expires);
        dbRef.update("expiresOn", cal.getTime());

        Toast.makeText(this, R.string.action_event_updated, Toast.LENGTH_SHORT).show();
    }

    private void deleteEvent(Event event){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(event.getEventID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.action_event_deleted, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to delete the event", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private float getCategoryHue(String category) {
        String [] cats = getResources().getStringArray(R.array.category_entries);
        if (category.equals(cats[0])) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (category.equals(cats[1])) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if (category.equals(cats[2])) {
            return BitmapDescriptorFactory.HUE_BLUE;
        } else { // others
            return BitmapDescriptorFactory.HUE_YELLOW;
        }
    }

    private boolean isEventWithinReach(Event event, float distance){
        if (filtersEnabled) {
            float [] results = new float[3];
            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, event.getLatitude(), event.getLongitude(), results);
            return (results[0] < distance);
        }

        return true;
    }

    private boolean isEventCategoryEnabled(Event event){
        if (filtersEnabled) {
            String cat = event.getCategory();
            return categoriesEnabled.contains(cat);
        }

        return true;
    }

    public void refreshEventsList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query eventsQuery = db.collection("events");
        if (filtersEnabled) {
            //double latMax = SphericalUtil.computeOffset(currentLocation, maxRadius, 0).latitude; // North
            //double longMax = SphericalUtil.computeOffset(currentLocation, maxRadius, 90).longitude; // East
            //double latMin = SphericalUtil.computeOffset(currentLocation, maxRadius, 180).latitude; // South
            //double longMin = SphericalUtil.computeOffset(currentLocation, maxRadius, 270).longitude; // West

            // Firestore queries do not allow 'where' to be used on more than one key.
            // Therefore we filter by 'category' on the server, and do the rest on client.
            eventsQuery = db.collection("events")
                    //.whereLessThan("latitude", latMax)
                    //.whereGreaterThan("latitude", latMin)
                    //.whereLessThan("longitude", longMax)
                    //.whereGreaterThan("longitude", longMin);
                    .whereIn("category", new ArrayList<>(categoriesEnabled));
        }

        eventsQuery
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allEventsInRadius.clear();
                            myEventsInRadius.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                if (event.getIsActive() && isEventWithinReach(event, maxRadius) && isEventCategoryEnabled(event)) {
                                    allEventsInRadius.add(event);
                                    float hue = getCategoryHue(event.getCategory());
                                    float alpha = 1.0f;
                                    if (event.getUserID().equals(loggedInUser.getUserID())){
                                        myEventsInRadius.add(event);
                                        alpha = 0.7f;
                                    }

                                    if (mMap != null) {
                                        LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                                        Marker mark =  mMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .icon(BitmapDescriptorFactory.defaultMarker(hue))
                                                .alpha(alpha));
                                        mark.setTag(event);
                                        mark.showInfoWindow();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to query events", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createEventDialogBox(Context context, final Event finalEvent){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.event_input_dialog, null);

        final Spinner spinnerCategories = dialogView.findViewById(R.id.spinner);
        final EditText etEventTitle = dialogView.findViewById(R.id.event_title);
        final EditText etEventDetails = dialogView.findViewById(R.id.event_details);
        final EditText etHoursToExpire = dialogView.findViewById(R.id.edt_expire);
        final Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btnDelete = dialogView.findViewById(R.id.btn_delete);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        ArrayAdapter aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, eventCategories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(aa);

        if (finalEvent != null) {
            btnSubmit.setText(getString(R.string.label_update));
            etEventTitle.setText(finalEvent.getTitle());
            etEventDetails.setText(finalEvent.getDetails());

            Calendar cal = Calendar.getInstance();
            Date date = finalEvent.getExpiresOn().toDate();
            long diffHours = (date.getTime() - cal.getTime().getTime()) / (60 * 60 * 1000);
            etHoursToExpire.setText(String.valueOf(diffHours));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etEventTitle.getText().toString().trim();
                String details = etEventDetails.getText().toString().trim();
                String category = String.valueOf(spinnerCategories.getSelectedItem());
                String expire = etHoursToExpire.getText().toString().trim();

                int hours = minHours;
                try {
                    hours = Integer.parseInt(expire);
                } catch (NumberFormatException ignored) {}

                if (finalEvent != null) {
                    updateEvent(finalEvent, title, details, category, hours);
                } else {
                    addEvent(homeMarker.getPosition(), title, details, category, Math.min(hours, maxHours));
                }
                dialogBuilder.dismiss();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.action_delete_confirm);
        builder.setIcon(R.drawable.ic_launcher_foreground);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteEvent(finalEvent);
                        dialog.dismiss();
                        dialogBuilder.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}