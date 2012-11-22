package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class SystemSettings extends SettingsPreferenceFragment {

    private static final String STATUSBAR_SETTINGS = "statusbar_settings";

    PreferenceScreen mStatusbarSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_settings);

        PreferenceScreen prefs = getPreferenceScreen();

	mStatusbarSettings = (PreferenceScreen) findPreference(STATUSBAR_SETTINGS);
    }
}
