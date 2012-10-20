/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.IWindowManager;
import android.os.ServiceManager;
import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;



import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class NavigationBar extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String NAVIGATION_BAR_SHOW = "navigation_bar_show";
    private static final String KEY_MENU_ENABLED = "key_menu_enabled";
    private static final String KEY_BACK_ENABLED = "key_back_enabled";
    private static final String KEY_HOME_ENABLED = "key_home_enabled";

    private CheckBoxPreference mNavigationBarShow;
    private CheckBoxPreference mMenuKeyEnabled;
    private CheckBoxPreference mBackKeyEnabled;
    private CheckBoxPreference mHomeKeyEnabled;

    private boolean mHasNavigationBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.navigation_bar);

        //Do we have a NavigationBar enabled in the running system?
        IWindowManager windowManager = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        try {
            mHasNavigationBar = windowManager.hasNavigationBar();
        } catch (RemoteException e) {
            mHasNavigationBar = false;
        }

        PreferenceScreen prefSet = getPreferenceScreen();

        mNavigationBarShow = (CheckBoxPreference) prefSet.findPreference(NAVIGATION_BAR_SHOW);
        mMenuKeyEnabled = (CheckBoxPreference) prefSet.findPreference(KEY_MENU_ENABLED);
        mBackKeyEnabled = (CheckBoxPreference) prefSet.findPreference(KEY_BACK_ENABLED);
        mHomeKeyEnabled = (CheckBoxPreference) prefSet.findPreference(KEY_HOME_ENABLED);

        mNavigationBarShow.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.NAVIGATION_BAR_SHOW, 0) == 1));
        if (mNavigationBarShow.isChecked()) {
            enableKeysPrefs();
        } else {
            resetKeys();
        }
    }

    public void enableKeysPrefs() {
        mMenuKeyEnabled.setEnabled(true);
        mBackKeyEnabled.setEnabled(true);
        mHomeKeyEnabled.setEnabled(true);
        mMenuKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_MENU_ENABLED, 1) == 1));
        mBackKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_BACK_ENABLED, 1) == 1));
        mHomeKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_HOME_ENABLED, 1) == 1));
    }

    public void resetKeys() {
        mMenuKeyEnabled.setEnabled(false);
        mBackKeyEnabled.setEnabled(false);
        mHomeKeyEnabled.setEnabled(false);
        mMenuKeyEnabled.setChecked(true);
        mBackKeyEnabled.setChecked(true);
        mHomeKeyEnabled.setChecked(true);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_MENU_ENABLED, 1);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_BACK_ENABLED, 1);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_HOME_ENABLED, 1);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mNavigationBarShow) {
            value = mNavigationBarShow.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_SHOW, value ? 1 : 0);
            if (value) {
                enableKeysPrefs();
            } else {
                resetKeys();
             }
             if (value != mHasNavigationBar) {
                 Toast toast = Toast.makeText(getActivity().getApplicationContext(), R.string.navigation_bar_reboot_message, Toast.LENGTH_LONG);
                 toast.show();
             }
            return true;
        } else if (preference == mMenuKeyEnabled) {
            value = mMenuKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_MENU_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mBackKeyEnabled) {
            value = mBackKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_BACK_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mHomeKeyEnabled) {
            value = mHomeKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_HOME_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }
}
