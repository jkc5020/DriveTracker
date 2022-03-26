package com.example.drivetracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.drivetracking.BE.Trip;
import com.example.drivetracking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ScrollView scrollView;
    LinearLayout driveList;
    ArrayList<Trip> trips;
    String sharedPrefs = "sharedPrefs";
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

   
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trips);
        editor.putString("trips", json);
        editor.apply();
    }

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
        loadData();
        driveList = (LinearLayout) findViewById(R.id.parent_linear_layout);
        Intent intent = getIntent();
        Double distance = intent.getDoubleExtra("miles", 0);
        String time = intent.getStringExtra("time");
        if(time != null) {
            Trip newTrip = new Trip(distance, time);
            trips.add(newTrip);
        }

        initViews();


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

    private void initViews(){
        for (Trip trip: trips) {
            View singleDrive = (View) getLayoutInflater().inflate(R.layout.single_drive, null,false);
            TextView miles = (TextView) singleDrive.findViewById(R.id.tripDistance);
            TextView gas = (TextView) singleDrive.findViewById(R.id.tripGas);
            TextView time = (TextView) singleDrive.findViewById(R.id.tripTime);
            miles.setText(String.valueOf(trip.getMiles()));
            gas.setText("empty");
            time.setText(trip.getTime());

            driveList.addView(singleDrive);

        }

    }
}