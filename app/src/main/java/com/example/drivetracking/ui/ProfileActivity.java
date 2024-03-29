package com.example.drivetracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.drivetracking.BE.Trip;
import com.example.drivetracking.R;
import com.example.drivetracking.ui.Util.recyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * ProfileActivity shows a log of all the drives made, as well
 * as cumulative statistics
 */
public class ProfileActivity extends AppCompatActivity {


    TextView totalMiles; // total miles logged
    TextView totalTime;  // total time logged
    TextView totalGas; // total gas used (currently not fully implemented)
    ArrayList<Trip> trips; // list of all trips
    String sharedPrefs = "sharedPrefs"; // String to find shared preferences
    private RecyclerView recyclerView; //Recyclerview to display drive log

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        initViews();
//    }


    /**
     * Saves data to sharedPreferences
     */
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trips);
        editor.putString("trips", json);
        editor.apply();
    }

    /**
     * Loads data from SharedPreferences
     */
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("trips", null);
        Type type = new TypeToken<ArrayList<Trip>>(){}.getType();
        trips = gson.fromJson(json, type);
        if(trips == null){
            trips = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recyclerView = findViewById(R.id.driveLog);
        totalMiles = (TextView) findViewById(R.id.totalDistance);
        totalTime = (TextView) findViewById(R.id.totalTime);
        totalGas = (TextView) findViewById(R.id.totalGallons);

        loadData();
        //driveList = (LinearLayout) findViewById(R.id.parent_linear_layout);
        Intent intent = getIntent();
        Double distance = intent.getDoubleExtra("miles", 0);
        String time = intent.getStringExtra("time");
        int seconds = intent.getIntExtra("seconds", 0);

        // if activity was not opened from saving a drive, then don't create a new trip to log
        if(time != null) {
            Trip newTrip = new Trip(distance, time, seconds);
            trips.add(newTrip);
        }

        initViews();

        // code to handle bottomNavigationView
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.profile:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        return true;

                }
                return false;
            }
        });
    }

    /**
     * Inits all views, using a recyclerAdapter for recyclerView
     */
    private void initViews(){
        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        recyclerAdapter adapter = new recyclerAdapter(trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        int tGallons = 0;
        String tTime;
        Double tMiles = 0.0;

        int tSeconds = 0;
        int size = trips.size();

        for (Trip trip: trips) {
            tSeconds += trip.getTotalSeconds();
            tMiles += trip.getMiles();

        }

        tTime = calculateTime(tSeconds);
        String sMiles = decimalFormat.format(tMiles) + " miles";
        String sGas = "0 gallons";
        totalTime.setText(tTime);
        totalGas.setText(sGas);
        totalMiles.setText(sMiles);


    }

    /**
     * Calculates the total time across all trips
     * @param seconds - total seconds
     * @return - Time represented as string
     */
    private String calculateTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
    }
}