package com.unimelb.niels.awaretest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aware.Applications;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.providers.Aware_Provider;
import com.aware.ui.PermissionsHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String studyUrl = "https://api.awareframework.com/index.php/webservice/index/1405/QrdrvuNylRGB";
    String TAG = "Plugin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAware();
    }

    private void loadAware() {
        Cursor study = getApplicationContext().getContentResolver().query(Aware_Provider.Aware_Studies.CONTENT_URI, null, null, null,
                Aware_Provider.Aware_Studies.STUDY_TIMESTAMP + " DESC LIMIT 1");

        Button join_study = (Button) findViewById(R.id.join_study);

        if (study != null && study.moveToFirst()) {
            // already joined
        } else {
            join_study.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Aware.isStudy(getApplicationContext())) {

                        Log.d(TAG, "JoinStudy onClick");

                        Aware.joinStudy(getApplicationContext(), studyUrl);

                        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, true);
                        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, true);

                        Aware.setSetting(getApplicationContext(), Aware_Preferences.WEBSERVICE_WIFI_ONLY, false);
                        Aware.setSetting(getApplicationContext(), Aware_Preferences.FREQUENCY_WEBSERVICE, 30);
                        Aware.setSetting(getApplicationContext(), Aware_Preferences.WEBSERVICE_SILENT, true);

                        Aware.startPlugin(getApplicationContext(), "com.unimelb.niels.awaretest");

                        //Ask accessibility to be activated
                        Applications.isAccessibilityServiceActive(getApplicationContext());

                        //Ask to ignore Doze
                        Aware.isBatteryOptimizationIgnored(getApplicationContext(), getApplicationContext().getPackageName());
                    }
                }
            });
        }

        if (study != null && !study.isClosed()) study.close();


        Button sync_data = (Button) findViewById(R.id.sync_data);
        sync_data.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent sync = new Intent(Aware.ACTION_AWARE_SYNC_DATA);
                sendBroadcast(sync);

                Toast.makeText(getApplicationContext(), "Syncing data...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("AWARE Niels", "onResume called: ");

        super.onResume();

        ArrayList<String> REQUIRED_PERMISSIONS = new ArrayList<>();
//        REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        REQUIRED_PERMISSIONS.add(Manifest.permission.ACCESS_FINE_LOCATION);
        REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            REQUIRED_PERMISSIONS.add(Manifest.permission.READ_CALL_LOG);
        }
        REQUIRED_PERMISSIONS.add(Manifest.permission.READ_CONTACTS);
        REQUIRED_PERMISSIONS.add(Manifest.permission.READ_SMS);
        REQUIRED_PERMISSIONS.add(Manifest.permission.INTERNET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            REQUIRED_PERMISSIONS.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.INTERNET);
        REQUIRED_PERMISSIONS.add(Manifest.permission.READ_PHONE_STATE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.RECORD_AUDIO);

        REQUIRED_PERMISSIONS.add(Manifest.permission.WAKE_LOCK);

        boolean permissions_ok = true;
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {

                Log.d("AWARE Niels", p);
                Log.d("AWARE Niels", "Permissions not OK");
                permissions_ok = false;
                break;
            }
        }

        if (permissions_ok) {
            Log.d("AWARE Niels", "permissions OK");

            // Initialise framework + assign UUID
            if (!Aware.isServiceRunning(this, Aware.class)) {
                Intent aware = new Intent(this, Aware.class);
                startService(aware);
            }

            Aware.setSetting(this, Aware_Preferences.DEBUG_FLAG, true);

        } else {
            Intent permissions = new Intent(this, PermissionsHandler.class);
            permissions.putExtra(PermissionsHandler.EXTRA_REQUIRED_PERMISSIONS, REQUIRED_PERMISSIONS);
            permissions.putExtra(PermissionsHandler.EXTRA_REDIRECT_ACTIVITY, getPackageName() + "/" + getClass().getName());
            permissions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(permissions);
        }

    }

}
