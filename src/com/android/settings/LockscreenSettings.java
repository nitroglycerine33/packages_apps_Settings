
package com.android.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;

public class LockscreenSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

        private static final String LOCKSCREEN_STYLES = "lockscreen_styles";
        private static final String ROTARY_ARROWS = "rotary_arrows";

        private CheckBoxPreference mRotaryArrows;
        private ListPreference mLockStyle;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.lockscreen_settings);

            PreferenceScreen prefSet = getPreferenceScreen();

            mLockStyle = (ListPreference) findPreference(LOCKSCREEN_STYLES);
            mLockStyle.setOnPreferenceChangeListener(this);
            mLockStyle.setValue(Settings.System.getInt(getActivity().getContentResolver(), Settings.System.LOCKSCREEN_TYPE,
                0) + "");

            mRotaryArrows = (CheckBoxPreference) prefSet.findPreference(ROTARY_ARROWS);
            mRotaryArrows.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_HIDE_ARROWS, 0) == 1);
        }

        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                Preference preference) {
            boolean value;
            if  (preference == mRotaryArrows) {
                value = mRotaryArrows.isChecked();
                Settings.System.putInt(getContentResolver(),
                        Settings.System.LOCKSCREEN_HIDE_ARROWS, value ? 1 : 0);
                return true;
            }
            return false;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mLockStyle) {
                Settings.System.putInt(getActivity().getContentResolver(), Settings.System.LOCKSCREEN_TYPE, Integer.parseInt((String) newValue));
                return true;
            }
            return false;
        }


    }

