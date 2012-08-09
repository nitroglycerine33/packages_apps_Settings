package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class GeneralSettings extends SettingsPreferenceFragment {

    private static final String QUIET_HOURS = "quiet_hours";
    private static final String WEATHER_SETTINGS = "weather_settings";

    PreferenceScreen mQuietHours;
    PreferenceScreen mWeatherSettings;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        mQuietHours = (PreferenceScreen) findPreference(QUIET_HOURS);
        mWeatherSettings = (PreferenceScreen) findPreference(WEATHER_SETTINGS);

    }
}
