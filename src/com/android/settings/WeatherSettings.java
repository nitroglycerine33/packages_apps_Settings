package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class WeatherSettings extends SettingsPreferenceFragment {

    private static final String WEATHER_SETTINGS = "weather_settings";

    PreferenceScreen mWeatherSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.weather_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        mWeatherSettings = (PreferenceScreen) findPreference(WEATHER_SETTINGS);

    }
}
