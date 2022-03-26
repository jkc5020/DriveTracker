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

public class StartDriveActivity extends AppCompatActivity {
    private Button startService;
    private Boolean serviceOn = false;
    public static TextView distance;
    public static TextView time;
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

        private void deActivateService () {
            Intent serviceIntent = new Intent(this, MyNavigationService.class);
            stopService(serviceIntent);

        }

        private void activateService () {
            Intent serviceIntent = new Intent(this, MyNavigationService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }