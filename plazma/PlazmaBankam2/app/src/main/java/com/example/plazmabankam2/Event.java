package com.example.plazmabankam2;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.Calendar;

public class Event {
    private String eventID = null;
    private String userID = null;
    private String userDisplayName = null;
    private boolean isActive = true;
    private double latitude;
    private double longitude;
    private String title = null;
    private String details = null;
    private String category = null;
    private Timestamp publishedOn = null;
    private Timestamp expiresOn = null;

    public Event(){

    }

    public String toString(){
        return this.getTitle();
    }

    public Event(String eventID, String userID, String userDisplayName, LatLng loc, String title, String details, String category, int expires) {
        this.eventID = eventID;
        this.userID = userID;
        this.userDisplayName = userDisplayName;
        this.isActive = true;
        this.latitude = loc.latitude;
        this.longitude = loc.longitude;
        this.title = title;
        this.details = details;
        this.category = category;
        Calendar cal = Calendar.getInstance();
        this.publishedOn = new Timestamp(cal.getTime());
        cal.add(Calendar.HOUR_OF_DAY, expires);
        this.expiresOn = new Timestamp(cal.getTime());
    }

    public String getEventID(){return eventID; }

    public String getUserID() { return userID; }

    public String getUserDisplayName() { return userDisplayName; }

    public boolean getIsActive() {
        return isActive;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getTitle(){return title; }

    public String getDetails(){
        return details;
    }

    public String getCategory(){return category; }

    public Timestamp getPublishedOn(){
        return publishedOn;
    }

    public Timestamp getExpiresOn(){
        return expiresOn;
    }
}

