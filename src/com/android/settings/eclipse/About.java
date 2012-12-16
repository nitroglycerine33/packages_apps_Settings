
package com.android.settings.eclipse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.util.devcard.DevCard;

public class About extends SettingsPreferenceFragment {
	
	Preference mEclipseSite;
	Preference mEclipseSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_settings);
        
        mEclipseSite = findPreference("eclipse_site");
        mEclipseSource = findPreference("eclipse_source");
        
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mEclipseSite) {
            gotoUrl("http://eclipserom.com");
        } else if (preference == mEclipseSource) {
        	gotoUrl("http://twitter.com/nitroglycerin33");
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    
    private void gotoUrl(String uri) {
    	Uri page = Uri.parse(uri);
        Intent i = new Intent(Intent.ACTION_VIEW, page);
        getActivity().startActivity(i);
    }
}
