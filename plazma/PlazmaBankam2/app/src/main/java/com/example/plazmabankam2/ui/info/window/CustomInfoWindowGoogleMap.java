package com.example.plazmabankam2.ui.info.window;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.plazmabankam2.Event;
import com.example.plazmabankam2.R;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView tvEventCategory = view.findViewById(R.id.info_window_event_category);
        TextView tvEventTitle = view.findViewById(R.id.info_window_event_title);
        TextView tvEventDetails = view.findViewById(R.id.info_window_event_details);

        TextView tvEventPublishDate = view.findViewById(R.id.info_window_event_published_on);
        TextView tvEventPublishedBy = view.findViewById(R.id.info_window_event_published_by);
        TextView tvEventExpiresIn = view.findViewById(R.id.info_window_event_expires_in);

        Object tag = marker.getTag();
        if (tag instanceof Event) {
            Event event = (Event) tag;

            String category = "Bölüm: " + event.getCategory();
            tvEventCategory.setText(category);
            tvEventTitle.setText(event.getTitle());
            tvEventDetails.setText(event.getDetails());

            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String publishedOn = "Yayınlandı: " + dateFormat.format(event.getPublishedOn().toDate());
            tvEventPublishDate.setText(publishedOn);
            String publishedBy = "Tarafından Yayınlandı: " + event.getUserDisplayName();
            tvEventPublishedBy.setText(publishedBy);

            Calendar cal = Calendar.getInstance();
            Date date = event.getExpiresOn().toDate();
            if (date != null) {
                long diff = date.getTime() - cal.getTime().getTime();
                String info = context.getString(R.string.label_event_expired);
                if (diff > 0) {
                    long diffHours = diff / (60 * 60 * 1000);
                    long diffMinutes = diff / (60 * 1000) % 60;
                    info = String.format(context.getString(R.string.label_event_expires_in), diffHours, diffMinutes);
                }
                tvEventExpiresIn.setText(info);
            }
        } else {
            tvEventTitle.setText(R.string.label_nothing_here_yet);
        }

        return view;
    }
}
