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
import android.widget.EditText;

public class NavigationBarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String SHOW_MENU_BUTTON = "show_menu_button";
    private static final String SHOW_SEARCH_BUTTON = "show_search_button";
    private static final String PREF_CARRIER_TEXT = "carrier_text";
    private CheckBoxPreference mShowMenuButton;
    private CheckBoxPreference mShowSearchButton;
    private Preference mCarrier;

    String mCarrierText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_bar_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mShowMenuButton = (CheckBoxPreference) prefSet.findPreference(SHOW_MENU_BUTTON);
        mShowMenuButton.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.SHOW_MENU_BUTTON, 0) == 1);

        mShowSearchButton = (CheckBoxPreference) prefSet.findPreference(SHOW_SEARCH_BUTTON);
        mShowSearchButton.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.SHOW_SEARCH_BUTTON, 0) == 1);

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
        if (preference == mShowMenuButton) {
            value = mShowMenuButton.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.SHOW_MENU_BUTTON, value ? 1 : 0);
            return true;
        } else if (preference == mShowSearchButton) {
            value = mShowSearchButton.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.SHOW_SEARCH_BUTTON, value ? 1 : 0);
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
        return false;
    }
}
