package com.android.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import java.util.List;

public class InputSettings extends SettingsPreferenceFragment {

private static final String VOLUME_WAKE_PREF = "pref_volume_wake";
private static final String KEY_VOLBTN_MUSIC_CTRL = "volbtn_music_controls";
private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";

private CheckBoxPreference mVolumeWakePref;
private CheckBoxPreference mVolBtnMusicCtrl;
private CheckBoxPreference mKillAppLongpressBack;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.input_settings);
        PreferenceScreen prefs = getPreferenceScreen();

	mVolumeWakePref = (CheckBoxPreference) prefs.findPreference(VOLUME_WAKE_PREF);
	mVolumeWakePref.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);

	mVolBtnMusicCtrl = (CheckBoxPreference) findPreference(KEY_VOLBTN_MUSIC_CTRL);
	mVolBtnMusicCtrl.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.VOLBTN_MUSIC_CONTROLS, 0) != 0);


        mKillAppLongpressBack = (CheckBoxPreference) findPreference(KILL_APP_LONGPRESS_BACK);
	mKillAppLongpressBack.setChecked(Settings.Secure.getInt(getContentResolver(), Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) == 1);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	boolean value;
        if (preference == mVolumeWakePref) {
	    value = mVolumeWakePref.isChecked();
            Settings.System.putInt(getContentResolver(),
	    Settings.System.VOLUME_WAKE_SCREEN, value ? 1 : 0);
	} else if (preference == mVolBtnMusicCtrl) {
	    value = mVolBtnMusicCtrl.isChecked(); 
	    Settings.System.putInt(getContentResolver(), 
	    Settings.System.VOLBTN_MUSIC_CONTROLS, value ? 1 : 0);
	} else if (preference == mKillAppLongpressBack) {
	    value = mKillAppLongpressBack.isChecked();
	    Settings.Secure.putInt(getContentResolver(),
		Settings.Secure.KILL_APP_LONGPRESS_BACK, value ? 1 : 0);
	    return true;
        }

        return true;
    }
}
