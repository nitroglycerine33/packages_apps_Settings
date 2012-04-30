package com.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.List;

public class EclipseSettings extends SettingsPreferenceFragment {

    private static final String POWER_WIDGETS = "power_widgets";
    private static final String SOFTKEY_SETTINGS = "softkey_settings";
    private static final String GENERAL_SETTINGS = "general_settings";
    private static final String KEY_QUIET_HOURS = "quiet_hours";
    private static final String LED_SETTINGS = "led_settings";

    PreferenceScreen mPowerWidgets;
    PreferenceScreen mSoftkeySettings;
    PreferenceScreen mGeneralSettings;
    PreferenceScreen mQuietHours;
    PreferenceScreen mLEDSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.eclipse_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        mSoftkeySettings = (PreferenceScreen) findPreference(SOFTKEY_SETTINGS);
        mPowerWidgets = (PreferenceScreen) findPreference(POWER_WIDGETS);
	mGeneralSettings = (PreferenceScreen) findPreference(GENERAL_SETTINGS);
	mQuietHours = (PreferenceScreen) findPreference(KEY_QUIET_HOURS);
        mLEDSettings = (PreferenceScreen) findPreference(LED_SETTINGS);

    }
}
