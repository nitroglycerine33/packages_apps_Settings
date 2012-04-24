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

public class NavigationBarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String SHOW_MENU_BUTTON = "show_menu_button";
    private static final String SHOW_SEARCH_BUTTON = "show_search_button";
    private static final String LONG_PRESS_HOMEKEY = "long_press_homekey";
    private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
    private CheckBoxPreference mShowMenuButton;
    private CheckBoxPreference mShowSearchButton;
    private CheckBoxPreference mLongPressHome;
    private CheckBoxPreference mKillAppLongpressBack;

    String mCarrierText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_bar_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mLongPressHome = (CheckBoxPreference) prefSet.findPreference(LONG_PRESS_HOMEKEY);
        mLongPressHome.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.LONG_PRESS_HOME, 0) == 1);

        mShowMenuButton = (CheckBoxPreference) prefSet.findPreference(SHOW_MENU_BUTTON);
        mShowMenuButton.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.SHOW_MENU_BUTTON, 0) == 1);

        mShowSearchButton = (CheckBoxPreference) prefSet.findPreference(SHOW_SEARCH_BUTTON);
        mShowSearchButton.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.SHOW_SEARCH_BUTTON, 0) == 1);
	updateLongPressToggle(mShowSearchButton.isChecked());
	updateSearchToggle(mLongPressHome.isChecked());

	mKillAppLongpressBack = (CheckBoxPreference) prefSet.findPreference(KILL_APP_LONGPRESS_BACK);
	mKillAppLongpressBack.setChecked(Settings.Secure.getInt(getContentResolver(),
	    Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) == 1);
    }

    private void updateSearchToggle(boolean bool) {
        if (bool)
            mShowSearchButton.setEnabled(false);
        else
            mShowSearchButton.setEnabled(true);
    }

    private void updateLongPressToggle(boolean bool) {
        if (bool)
            mLongPressHome.setEnabled(false);
        else
            mLongPressHome.setEnabled(true);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mShowMenuButton) {
            value = mShowMenuButton.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.SHOW_MENU_BUTTON, value ? 1 : 0);
            return true;
        } else if (preference == mShowSearchButton) {
            value = mShowSearchButton.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.SHOW_SEARCH_BUTTON, value ? 1 : 0);
		updateLongPressToggle(value);
            return true;
        } else if (preference == mLongPressHome) {
            value = mLongPressHome.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.LONG_PRESS_HOME, value ? 1 : 0);
            updateSearchToggle(value);
            return true;
	} else if (preference == mKillAppLongpressBack) {
	    value = mKillAppLongpressBack.isChecked();
	    Settings.Secure.putInt(getContentResolver(),
		Settings.Secure.KILL_APP_LONGPRESS_BACK, value ? 1 : 0);
	    return true;
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
