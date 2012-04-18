package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class GeneralSettings extends SettingsPreferenceFragment {

private static final String KEY_QUIET_HOURS = "quiet_hours";

private PreferenceScreen mQuietHours;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);

        PreferenceScreen prefs = getPreferenceScreen();

	mQuietHours = (PreferenceScreen) findPreference(KEY_QUIET_HOURS);

    }
}
