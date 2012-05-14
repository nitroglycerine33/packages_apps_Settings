package com.android.settings;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.Spannable;
import android.widget.EditText;

import java.util.List;

public class GeneralSettings extends SettingsPreferenceFragment {


private static final String VOLUME_WAKE_PREF = "pref_volume_wake";
private static final String KEY_VOLBTN_MUSIC_CTRL = "volbtn_music_controls";
private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
private static final String PREF_CARRIER_TEXT = "carrier_text";
private static final String PREF_IME_SWITCHER = "ime_switcher";

private CheckBoxPreference mKillAppLongpressBack;
private CheckBoxPreference mShowImeSwitcher;
private CheckBoxPreference mVolumeWakePref;
private CheckBoxPreference mVolBtnMusicCtrl;

private Preference mCarrier;

String mCarrierText = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);
        PreferenceScreen prefs = getPreferenceScreen();

	mVolumeWakePref = (CheckBoxPreference) prefs.findPreference(VOLUME_WAKE_PREF);
	mVolumeWakePref.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);

	mVolBtnMusicCtrl = (CheckBoxPreference) findPreference(KEY_VOLBTN_MUSIC_CTRL);
	mVolBtnMusicCtrl.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.VOLBTN_MUSIC_CONTROLS, 0) != 0);

	mKillAppLongpressBack = (CheckBoxPreference) prefs.findPreference(KILL_APP_LONGPRESS_BACK);
	mKillAppLongpressBack.setChecked(Settings.Secure.getInt(getContentResolver(),
	    Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) == 1);
	mShowImeSwitcher = (CheckBoxPreference) findPreference(PREF_IME_SWITCHER);
	mShowImeSwitcher.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
	    Settings.System.SHOW_STATUSBAR_IME_SWITCHER, 0) == 1);

	mCarrier = (Preference) prefs.findPreference(PREF_CARRIER_TEXT);
	updateCarrierText();
    }

    private void updateCarrierText() {
        mCarrierText = Settings.System.getString(getContentResolver(), Settings.System.CUSTOM_CARRIER_LABEL);
        if (mCarrierText == null) {
            mCarrier.setSummary("Upon changing you will need to wipe data to return to stock. Requires reboot.");
        } else {
            mCarrier.setSummary(mCarrierText);
        }
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
        } else if (preference == mShowImeSwitcher) {
	    boolean checked = ((CheckBoxPreference) preference).isChecked();
	    Settings.System.putInt(getActivity().getContentResolver(),
		Settings.System.SHOW_STATUSBAR_IME_SWITCHER, checked ? 1 : 0);
	    return true;
        } else if (preference == mCarrier) {
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle("Custom Carrier Text");
            ad.setMessage("Enter new carrier text here");
            final EditText text = new EditText(getActivity());
            text.setText(mCarrierText != null ? mCarrierText : "");
            ad.setView(text);
            ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = ((Spannable) text.getText()).toString();
                    Settings.System.putString(getActivity().getContentResolver(), Settings.System.CUSTOM_CARRIER_LABEL, value);
                    updateCarrierText();
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            ad.show();
        }

        return true;
    }
}
