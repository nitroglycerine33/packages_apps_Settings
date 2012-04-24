package com.android.settings;
import com.android.settings.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.text.Spannable;
import android.widget.EditText;

public class SystemUISettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PREF_CARRIER_TEXT = "carrier_text";
    private static final String PREF_IME_SWITCHER = "ime_switcher";
    private static final String PREF_CLOCK_DISPLAY_STYLE = "clock_am_pm";
    private static final String PREF_CLOCK_STYLE = "clock_style";
    private Preference mCarrier;
    private CheckBoxPreference mShowImeSwitcher;
    private ListPreference mAmPmStyle;
    private ListPreference mClockStyle;

    String mCarrierText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.systemui_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mClockStyle = (ListPreference) prefSet.findPreference(PREF_CLOCK_STYLE);
        mAmPmStyle = (ListPreference) prefSet.findPreference(PREF_CLOCK_DISPLAY_STYLE);

        int styleValue = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM, 2);
        mAmPmStyle.setValueIndex(styleValue);
        mAmPmStyle.setOnPreferenceChangeListener(this);

        int clockVal = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK, 1);
        mClockStyle.setValueIndex(clockVal);
        mClockStyle.setOnPreferenceChangeListener(this);

	mShowImeSwitcher = (CheckBoxPreference) findPreference(PREF_IME_SWITCHER);
	mShowImeSwitcher.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
	    Settings.System.SHOW_STATUSBAR_IME_SWITCHER, 0) == 1);

	mCarrier = (Preference) prefSet.findPreference(PREF_CARRIER_TEXT);
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

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mShowImeSwitcher) {
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
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
         if (preference == mAmPmStyle) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM, statusBarAmPm);
            return true;
        } else if (preference == mClockStyle) {
            int val = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK, val);
            return true;
        }
        return false;
    }
}
