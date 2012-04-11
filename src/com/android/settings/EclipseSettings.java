package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class EclipseSettings extends SettingsPreferenceFragment {

    private static final String POWER_WIDGETS = "power_widgets";
    private static final String SOFTKEY_SETTINGS = "softkey_settings";

    PreferenceScreen mPowerWidgets;
    PreferenceScreen mSoftkeySettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.eclipse_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        mSoftkeySettings = (PreferenceScreen) findPreference(SOFTKEY_SETTINGS);
        mPowerWidgets = (PreferenceScreen) findPreference(POWER_WIDGETS);

    }
}
