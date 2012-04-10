package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class EclipseSettings extends SettingsPreferenceFragment {

    private static final String POWER_WIDGETS = "power_widgets";

    PreferenceScreen mPowerWidgets;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.eclipse_settings);

        PreferenceScreen prefs = getPreferenceScreen();


        mPowerWidgets = (PreferenceScreen) findPreference(POWER_WIDGETS);

    }
}
