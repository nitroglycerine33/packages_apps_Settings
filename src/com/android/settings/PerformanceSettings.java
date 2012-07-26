package com.android.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PerformanceSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String CURRENT_KERNEL = "current_kernel";
    public static final String CURRENT_KERNEL_FILE = "/proc/version";
    public static final String CURRENT_CPU_SPEED = "current_cpu_speed";
    public static final String CURRENT_CPU_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    public static final String GOV_PREFERENCE = "gov_preference";
    public static final String GOVERNORS_LIST_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    public static final String GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String IO_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
    public static final String IO_PREFERENCE = "io_preference";
    public static final String MIN_FREQ_PREFERENCE = "min_freq_preference";
    public static final String MAX_FREQ_PREFERENCE = "max_freq_preference";
    public static final String FREQ_LIST_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String FREQ_MAX_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String FREQ_MIN_FILE = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String SOB_PREFERENCE = "sob_preference";

    private static final String TAG = "PerformanceSettings";

    private String mGovernorFormat;
    private String mIOFormat;
    private String mMinFrequencyFormat;
    private String mMaxFrequencyFormat;

    private Preference mCurKernel;
    private Preference mCurFreq;
    private ListPreference mGovernorPref;
    private ListPreference mIOPref;
    private ListPreference mMinFrequencyPref;
    private ListPreference mMaxFrequencyPref;

    private class CurCPUThread extends Thread {
        private boolean mInterrupt = false;

        public void interrupt() {
            mInterrupt = true;
        }

        @Override
        public void run() {
            try {
                while (!mInterrupt) {
                    sleep(500);
                    final String curFreq = readOneLine(CURRENT_CPU_FILE);
                    mCurCPUHandler.sendMessage(mCurCPUHandler.obtainMessage(0, curFreq));
                }
            } catch (InterruptedException e) {
            }
        }
    };

    private CurCPUThread mCurCPUThread = new CurCPUThread();

    private Handler mCurCPUHandler = new Handler() {
        public void handleMessage(Message msg) {
            mCurFreq.setSummary(toMHz((String) msg.obj));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGovernorFormat = getString(R.string.cpu_governors_summary);
        mIOFormat = getString (R.string.io_scheduler_summary);
        mMinFrequencyFormat = getString(R.string.cpu_min_freq_summary);
        mMaxFrequencyFormat = getString(R.string.cpu_max_freq_summary);

        String[] availableGovernors = readOneLine(GOVERNORS_LIST_FILE).split(" ");
        String[] availableFrequencies = new String[0];
        String availableFrequenciesLine = readOneLine(FREQ_LIST_FILE);
        if (availableFrequenciesLine != null)
             availableFrequencies = availableFrequenciesLine.split(" ");
        String[] frequencies;
        String temp;

        frequencies = new String[availableFrequencies.length];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = toMHz(availableFrequencies[i]);
        }

        String ioscheduler = getIOScheduler();
        String[] ioschedulers = getAvailableIOSchedulers();
        addPreferencesFromResource(R.xml.performance_settings);

        PreferenceScreen PrefScreen = getPreferenceScreen();

        temp = readOneLine(CURRENT_KERNEL_FILE);

        mCurKernel = (Preference) PrefScreen.findPreference(CURRENT_KERNEL);
        mCurKernel.setSummary(temp);

        temp = readOneLine(CURRENT_CPU_FILE);

        mCurFreq = (Preference) PrefScreen.findPreference(CURRENT_CPU_SPEED);
        mCurFreq.setSummary(toMHz(temp));

        temp = readOneLine(GOVERNOR);

        mGovernorPref = (ListPreference) PrefScreen.findPreference(GOV_PREFERENCE);
        mGovernorPref.setEntryValues(availableGovernors);
        mGovernorPref.setEntries(availableGovernors);
        mGovernorPref.setValue(temp);
        mGovernorPref.setSummary(String.format(mGovernorFormat, temp));
        mGovernorPref.setOnPreferenceChangeListener(this);

        temp = ioscheduler;

        mIOPref = (ListPreference) PrefScreen.findPreference(IO_PREFERENCE);
        mIOPref.setEntryValues(ioschedulers);
        mIOPref.setEntries(ioschedulers);
        mIOPref.setValue(temp);
        mIOPref.setSummary(String.format(mIOFormat, temp));
        mIOPref.setOnPreferenceChangeListener(this);

        temp = readOneLine(FREQ_MIN_FILE);

        mMinFrequencyPref = (ListPreference) PrefScreen.findPreference(MIN_FREQ_PREFERENCE);
        mMinFrequencyPref.setEntryValues(availableFrequencies);
        mMinFrequencyPref.setEntries(frequencies);
        mMinFrequencyPref.setValue(temp);
        mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat, toMHz(temp)));
        mMinFrequencyPref.setOnPreferenceChangeListener(this);

        temp = readOneLine(FREQ_MAX_FILE);

        mMaxFrequencyPref = (ListPreference) PrefScreen.findPreference(MAX_FREQ_PREFERENCE);
        mMaxFrequencyPref.setEntryValues(availableFrequencies);
        mMaxFrequencyPref.setEntries(frequencies);
        mMaxFrequencyPref.setValue(temp);
        mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat, toMHz(temp)));
        mMaxFrequencyPref.setOnPreferenceChangeListener(this);

        mCurCPUThread.start();
    }

    @Override
    public void onResume() {
        String temp;

        super.onResume();

        temp = readOneLine(FREQ_MAX_FILE);
        mMaxFrequencyPref.setValue(temp);
        mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat, toMHz(temp)));

        temp = readOneLine(FREQ_MIN_FILE);
        mMinFrequencyPref.setValue(temp);
        mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat, toMHz(temp)));

        temp = readOneLine(GOVERNOR);
        mGovernorPref.setSummary(String.format(mGovernorFormat, temp));

        String ioscheduler = getIOScheduler();
        temp = ioscheduler;
        mIOPref.setSummary(String.format(mIOFormat, temp));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCurCPUThread.interrupt();
        try {
            mCurCPUThread.join();
        } catch (InterruptedException e) {
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String fname = "";

        if (newValue != null) {
            if (preference == mGovernorPref) {
                fname = GOVERNOR;
            } else if (preference == mIOPref) {
                fname = IO_SCHEDULER;
            } else if (preference == mMinFrequencyPref) {
                fname = FREQ_MIN_FILE;
            } else if (preference == mMaxFrequencyPref) {
                fname = FREQ_MAX_FILE;
            }

            if (writeOneLine(fname, (String) newValue)) {
                if (preference == mGovernorPref) {
                    mGovernorPref.setSummary(String.format(mGovernorFormat, (String) newValue));
                } else if (preference == mIOPref) {
                    mIOPref.setSummary(String.format(mIOFormat, (String) newValue));
                } else if (preference == mMinFrequencyPref) {
                    mMinFrequencyPref.setSummary(String.format(mMinFrequencyFormat,
                            toMHz((String) newValue)));
                } else if (preference == mMaxFrequencyPref) {
                    mMaxFrequencyPref.setSummary(String.format(mMaxFrequencyFormat,
                            toMHz((String) newValue)));
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String readOneLine(String fname) {
        BufferedReader br;
        String line = null;

        try {
            br = new BufferedReader(new FileReader(fname), 512);
            try {
                line = br.readLine();
            } finally {
                br.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "IO Exception when reading /sys/ file", e);
        }
        return line;
    }

    public static boolean writeOneLine(String fname, String value) {
        try {
            FileWriter fw = new FileWriter(fname);
            try {
                fw.write(value);
            } finally {
                fw.close();
            }
        } catch (IOException e) {
            String Error = "Error writing to " + fname + ". Exception: ";
            Log.e(TAG, Error, e);
            return false;
        }
        return true;
    }

    public static String[] getAvailableIOSchedulers() {
        String [] schedulers = null;
        String [] aux = readStringArray(IO_SCHEDULER);
        if (aux != null) {
            schedulers = new String[aux.length];
            for (int i = 0; i < aux.length; i++) {
                if (aux[i].charAt(0) == '[') {
                    schedulers[i] = aux[i].substring(1, aux[i].length() - 1);
                } else {
                    schedulers[i] = aux[i];
                }
            }
        }
        return schedulers;
    }

    private static String[] readStringArray(String fname) {
        String line = readOneLine(fname);
        if (line != null) {
            return line.split(" ");
        }
        return null;
    }

    public static String getIOScheduler() {
        String scheduler = null;
        String[] schedulers = readStringArray(IO_SCHEDULER);
        if (schedulers != null) {
            for (String s : schedulers) {
                if (s.charAt(0) == '[') {
                    scheduler = s.substring(1, s.length() - 1);
                    break;
                }
            }
        }
        return scheduler;
    }

    private String toMHz(String mhzString) {
        return new StringBuilder().append(Integer.valueOf(mhzString) / 1000).append(" MHz").toString();
    }
}

