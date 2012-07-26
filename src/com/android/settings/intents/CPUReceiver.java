/*
 * Copyright (C) 2011 The CyanogenMod Project
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

package com.android.settings.intents;

import com.android.settings.PerformanceSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class CPUReceiver extends BroadcastReceiver {

    private static final String TAG = "CPUSettings";

    private static final String CPU_SETTINGS_PROP = "sys.cpufreq.restored";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (SystemProperties.getBoolean(CPU_SETTINGS_PROP, false) == false
                && intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
            SystemProperties.set(CPU_SETTINGS_PROP, "true");
            configureCPU(ctx);
        } else {
            SystemProperties.set(CPU_SETTINGS_PROP, "false");
        }
    }

    private void configureCPU(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        if (prefs.getBoolean(PerformanceSettings.SOB_PREFERENCE, false) == false) {
            Log.i(TAG, "Restore disabled by user preference.");
            return;
        }

        String governor = prefs.getString(PerformanceSettings.GOV_PREFERENCE, null);
        String minFrequency = prefs.getString(PerformanceSettings.MIN_FREQ_PREFERENCE, null);
        String maxFrequency = prefs.getString(PerformanceSettings.MAX_FREQ_PREFERENCE, null);
        String ioPreference = prefs.getString(PerformanceSettings.IO_PREFERENCE, null);
        String availableFrequenciesLine = PerformanceSettings.readOneLine(PerformanceSettings.FREQ_LIST_FILE);
        String availableGovernorsLine = PerformanceSettings.readOneLine(PerformanceSettings.GOVERNORS_LIST_FILE);
        boolean noSettings = ((availableGovernorsLine == null) || (governor == null)) && 
                             ((availableFrequenciesLine == null) || ((minFrequency == null) && (maxFrequency == null)));
        List<String> frequencies = null;
        List<String> governors = null;

        if (noSettings) {
            Log.d(TAG, "No settings saved. Nothing to restore.");
        } else {
            if (availableGovernorsLine != null){
                governors = Arrays.asList(availableGovernorsLine.split(" "));  
            }
            if (availableFrequenciesLine != null){
                frequencies = Arrays.asList(availableFrequenciesLine.split(" "));  
            }
            if (governor != null && governors != null && governors.contains(governor)) {
                PerformanceSettings.writeOneLine(PerformanceSettings.GOVERNOR, governor);
            }
            if (maxFrequency != null && frequencies != null && frequencies.contains(maxFrequency)) {
                PerformanceSettings.writeOneLine(PerformanceSettings.FREQ_MAX_FILE, maxFrequency);
            }
            if (minFrequency != null && frequencies != null && frequencies.contains(minFrequency)) {
                PerformanceSettings.writeOneLine(PerformanceSettings.FREQ_MIN_FILE, minFrequency);
            }
            // This is optimistic that this
            // is always going to exist..
            // haven't encountered any problems
            // in testing..
            PerformanceSettings.writeOneLine(PerformanceSettings.IO_SCHEDULER, ioPreference);
            Log.d(TAG, "CPU settings restored.");
        }
    }
}
