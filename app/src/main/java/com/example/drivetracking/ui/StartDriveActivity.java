package com.example.drivetracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.drivetracking.BE.Services.MyNavigationService;
import com.example.drivetracking.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity is used for a drive in progress,
 * displays total time and total miles, and uses MyNavigationService to track
 * location and time
 */
public class StartDriveActivity extends AppCompatActivity {
    private Button startService;        //button to start/end the drive
    private Boolean serviceOn = false; // tracks if service is running
    public static TextView distance;    // displays distance
    public static TextView time;        // displays time

    /**
     * Creates the activity by initializing all views, waits for user
     * to push button to activate service
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_drive);
        startService = (Button) findViewById(R.id.start);
        distance = (TextView) findViewById(R.id.distance);
        time = (TextView) findViewById(R.id.time);

        //time.setText("00:00:00");
        startService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!serviceOn) {
                    serviceOn = true;
                    //handles permission checks
                    if (ContextCompat.checkSelfPermission(StartDriveActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                        if (ActivityCompat.shouldShowRequestPermissionRationale(StartDriveActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)){
                            ActivityCompat.requestPermissions(StartDriveActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                        }else{
                            ActivityCompat.requestPermissions(StartDriveActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    }

                    startService.setBackgroundColor(getResources().getColor(R.color.white));
                    activateService();
                }
                else{
                    serviceOn = false;
                    deActivateService();
                }
            }
        });
    }

    /**
     * More permission checks
     * @param requestCode -
     * @param permissions -
     * @param grantResults -
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case 1: {

                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        if (ContextCompat.checkSelfPermission(StartDriveActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
            }

        }


    /**
     * Turns off the service
     */
    private void deActivateService () {
            Intent serviceIntent = new Intent(this, MyNavigationService.class);
            stopService(serviceIntent);

        }

    /**
     * Turns on the service
     */
    private void activateService () {
            Intent serviceIntent = new Intent(this, MyNavigationService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }