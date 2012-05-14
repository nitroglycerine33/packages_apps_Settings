package com.android.settings;
import com.android.settings.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.text.Spannable;

import com.android.settings.util.colorpicker.ColorPickerPreference;

import java.util.List;

public class ColorControlSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    
    private static final String SOFTKEY_COLOR = "softkey_color";
    private static final String UI_WIDGET_COLOR = "widget_color";
    private static final String LED_SETTINGS = "led_settings";
    private static final String CLOCK_COLOR = "clock_color";

    
    private ColorPickerPreference mSoftKeyColor;
    private ColorPickerPreference mWidgetColor;
    private ColorPickerPreference mClockColor;

    PreferenceScreen mLEDSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.color_control_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

	mClockColor = (ColorPickerPreference) prefSet.findPreference(CLOCK_COLOR);
	mClockColor.setOnPreferenceChangeListener(this);

        mWidgetColor = (ColorPickerPreference) prefSet.findPreference(UI_WIDGET_COLOR);
        mWidgetColor.setOnPreferenceChangeListener(this);

        mSoftKeyColor = (ColorPickerPreference) prefSet.findPreference(SOFTKEY_COLOR);
        mSoftKeyColor.setOnPreferenceChangeListener(this);

        mLEDSettings = (PreferenceScreen) findPreference(LED_SETTINGS);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSoftKeyColor) {
            String hexColor = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hexColor);
            int color = ColorPickerPreference.convertToColorInt(hexColor);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SOFT_KEY_COLOR, color);
            return true;
        } else if (preference == mClockColor) {
            String hexColor = ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hexColor);
            int color = ColorPickerPreference.convertToColorInt(hexColor);
            Settings.System.putInt(getContentResolver(),
                Settings.System.CLOCK_COLOR, color);
            return true;
        } else if (preference == mWidgetColor) {
        	String hexColor = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hexColor);
            int color = ColorPickerPreference.convertToColorInt(hexColor);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.EXPANDED_VIEW_WIDGET_COLOR, color);
            return true;
	}
        return false;
    }
}
