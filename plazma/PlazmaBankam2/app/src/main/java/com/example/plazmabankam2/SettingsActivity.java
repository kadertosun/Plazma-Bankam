package com.example.plazmabankam2;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

//import androidx.core.app.NavUtils;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_NOTIFICATION_SWITCH = "notification";
    public static final String KEY_PREF_FILTER_SWITCH = "filter";
    public static final String KEY_PREF_DISTANCE_EDIT_TEXT = "distance";
    public static final String KEY_PREF_CATEGORIES_MULTI_SELECT_LIST = "categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            EditTextPreference editTextPreference = getPreferenceManager().findPreference(KEY_PREF_DISTANCE_EDIT_TEXT);
            assert editTextPreference != null;
            editTextPreference.setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    //editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }*/
}
