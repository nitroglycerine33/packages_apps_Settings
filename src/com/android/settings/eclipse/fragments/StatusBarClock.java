
package com.android.settings.eclipse.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class StatusBarClock extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String PREF_ALARM_ENABLE = "alarm";
    private static final String STATUS_BAR_AM_PM = "status_bar_am_pm";
    private static final String STATUS_BAR_CLOCK_STYLE = "status_bar_clock_style";

    CheckBoxPreference mAlarm;
    private ListPreference mStatusBarClockStyle;
    private ListPreference mStatusBarAmPm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_statusbar_clock);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_statusbar_clock);

	PreferenceScreen prefSet = getPreferenceScreen();

        mStatusBarClockStyle = (ListPreference) prefSet.findPreference(STATUS_BAR_CLOCK_STYLE);
        mStatusBarAmPm = (ListPreference) prefSet.findPreference(STATUS_BAR_AM_PM);

        mAlarm = (CheckBoxPreference) findPreference(PREF_ALARM_ENABLE);
        mAlarm.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_SHOW_ALARM, 1) == 1);

        try {
            if (Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.TIME_12_24) == 24) {
                mStatusBarAmPm.setEnabled(false);
                mStatusBarAmPm.setSummary(R.string.status_bar_am_pm_info);
            }
        } catch (SettingNotFoundException e ) {
        }

        int statusBarClockStyle = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK_STYLE, 1);
        mStatusBarClockStyle.setValue(String.valueOf(statusBarClockStyle));
        mStatusBarClockStyle.setSummary(mStatusBarClockStyle.getEntry());
        mStatusBarClockStyle.setOnPreferenceChangeListener(this);

        int statusBarAmPm = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM, 2);
        mStatusBarAmPm.setValue(String.valueOf(statusBarAmPm));
        mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntry());
        mStatusBarAmPm.setOnPreferenceChangeListener(this);

    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == mAlarm) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_SHOW_ALARM, checked ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarClockStyle) {
            int statusBarClockStyle = Integer.valueOf((String) newValue);
            int index = mStatusBarClockStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK_STYLE, statusBarClockStyle);
            mStatusBarClockStyle.setSummary(mStatusBarClockStyle.getEntries()[index]);
            return true;
        } else if (preference == mStatusBarAmPm) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            int index = mStatusBarAmPm.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_AM_PM, statusBarAmPm);
            mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntries()[index]);
            return true;
        }
        return false;
    }
}
