package com.unimelb.niels.awaretest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;

/**
 * Created by nielsv on 08-14-2017.
 */

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Activate/deactivate plugin
     */
    public static final String STATUS_PLUGIN = "status_plugin";

    private CheckBoxPreference status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status = (CheckBoxPreference) findPreference(STATUS_PLUGIN);
        if (Aware.getSetting(this, STATUS_PLUGIN).length() == 0) {
            Aware.setSetting(this, STATUS_PLUGIN, true);
        }
        status.setChecked(Aware.getSetting(this, STATUS_PLUGIN).equals("true"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(STATUS_PLUGIN)) {
            if (sharedPreferences.getBoolean(key, false)) {
                Aware.setSetting(this, key, true);
                Aware.startPlugin(this, "com.unimelb.niels.awaretest");
            } else {
                Aware.setSetting(this, key, false);
                Aware.stopPlugin(this, "com.unimelb.niels.awaretest");
            }
        }

    }
}
