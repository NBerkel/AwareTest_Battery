package com.unimelb.niels.awaretest;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.utils.Aware_Plugin;


/**
 * Created by nielsv on 08-14-2017.
 */

public class Plugin extends Aware_Plugin {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Plugin onCreate called");

        TAG = "Plugin";

        DATABASE_TABLES = Provider.DATABASE_TABLES;
        TABLES_FIELDS = Provider.TABLES_FIELDS;
//        CONTEXT_URIS = new Uri[]{Provider.Symptom_Data.CONTENT_URI, Provider.Motivational_Data.CONTENT_URI};
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "Plugin onStartCommand called");

        if (PERMISSIONS_OK) {

            Aware.setSetting(this, Settings.STATUS_PLUGIN, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.WEBSERVICE_WIFI_ONLY, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.WEBSERVICE_FALLBACK_NETWORK, 6);

//            Aware.setSetting(getApplicationContext(), Aware_Preferences., 6);

            if (intent != null && intent.getExtras() != null && intent.getBooleanExtra("schedule", false)) {
//                try {
//                    Scheduler.removeSchedule(getApplicationContext(), "cancer_survey_morning");
//                    Scheduler.Schedule schedule = new Scheduler.Schedule("cancer_survey_morning");
//                    schedule.addHour(morning_hour)
//                            .addMinute(morning_minute)
//                            .setActionIntentAction(Plugin.ACTION_CANCER_SURVEY)
//                            .setActionType(Scheduler.ACTION_TYPE_BROADCAST);
//                    Scheduler.saveSchedule(this, schedule);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
            }

            Aware.startAWARE(this);
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Aware.setSetting(this, Settings.STATUS_PLUGIN, false);
        Aware.stopAWARE(this);
    }
}
