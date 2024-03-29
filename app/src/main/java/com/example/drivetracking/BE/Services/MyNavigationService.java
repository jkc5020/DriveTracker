package com.example.drivetracking.BE.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.drivetracking.BE.Trip;
import com.example.drivetracking.R;
import com.example.drivetracking.ui.ProfileActivity;
import com.example.drivetracking.ui.StartDriveActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * NavicationService that extends the service class to use a foreground service,
 * and implements the LocationListener to track the user's location for a given trip.
 */
public class MyNavigationService extends Service implements LocationListener {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";  //Channel Id for notification
    private LocationManager locationManager;                            //Location manager
    private FusedLocationProviderClient fusedLocationProviderClient;    //Google play services feature that finds best location based
                                                                        //on cellular/internet/ gps
    private Location prevLocation;                                      //previous location that was logged
    private Location currentLocation;                                   //current location of phone
    private double totalDistance = 0.0;                                 //total distance that has been logged
    private int seconds = 0;                                            // total seconds
    private boolean serviceRunning;
    private boolean wasRunning;
    private final Handler handler = new Handler();                      //Handler to manage Runnable for stopwatch
    String time;                                                        //Time of trip
    Runnable runnable;                                                  //Runnable for stopwatch

    /**
     * Callback to manage a new location, calls helper method to calculate distance from last
     * location to current one
     */
    private LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if(locationResult == null){
                return;
            }
            for(Location location : locationResult.getLocations()){
                System.out.println("-------");
                System.out.println(location.getLongitude());
                System.out.println(location.getLatitude());
                calculateDistance(location);
//                StartDriveActivity.distance.setText(String.valueOf(location.getLatitude()));
//                StartDriveActivity.time.setText(String.valueOf(location.getLongitude()));
            }
        }
    };

    /**
     * Calculate's the distance from last location to this location
     * @param location - Location object that was just logged
     */
    private void calculateDistance(Location location) {
        currentLocation = location;
        double meters = location.distanceTo(prevLocation);

        // ensures that distance is only logged if phone actually moved and the gps wasn't calibrating
        if(meters > 3) {
            totalDistance += (meters * 0.000621371192237334);
            DecimalFormat decimalFormat = new DecimalFormat("######.##");
            StartDriveActivity.distance.setText(decimalFormat.format(totalDistance) + "miles");
            prevLocation = currentLocation;
        }
        meters = 0;
    }

    private LocationRequest locationRequest;

    /**
     * Creates instance of the service
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        serviceRunning = true;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            System.out.println(location.getLatitude());
                            System.out.println(location.getLongitude());
                            prevLocation = location;
                            currentLocation = location;
                        }
                    }
                });

        /**
         * Runnable created to operate stopwatch on background thread
         */
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                if (serviceRunning) {
                    seconds++;
                }
                StartDriveActivity.time.setText(time);
                handler.postDelayed(this, 1000);



            }
        });
    }

    /**
     * Destroys service when trip is done
     */
    @Override
    public void onDestroy() {
        stopLocationUpdates();
        serviceRunning = false;
        //seconds = 0;
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("miles", totalDistance);
        intent.putExtra("time", time);
        intent.putExtra("seconds", seconds);
        seconds = 0;
        super.onDestroy();
        startActivity(intent);
    }

    /**
     * Checks settings of device to ensure that locations can start being used
     */
    private void checkSettings(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task <LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //settings of device are satisfied and we can start location updates
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //ask user to turn on permissions
                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    //apiException.startResolutionForResult(StartDriveActivity.this, 1001);
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Starts service
     * @param intent - intent of service
     * @param flags -
     * @param startId -
     * @return - START_NOT_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, StartDriveActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        startForeground(1, notification);
        checkSettings();
        return START_NOT_STICKY;

    }

    private void receiveLocationUpdates(){


    }

    /**
     * Creates notification channel
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }

    //// Methods required for interface
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
