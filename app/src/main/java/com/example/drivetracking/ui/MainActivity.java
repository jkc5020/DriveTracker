package com.example.drivetracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drivetracking.BE.Car;
import com.example.drivetracking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity shows a button to add a car along with EditText Views
 * to enter that information, as well as a button to start a drive
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        Button button = (Button) findViewById(R.id.startDrive);
        Button addCar = (Button) findViewById(R.id.addCar);
        EditText make = (EditText) findViewById(R.id.make);
        EditText model = (EditText) findViewById(R.id.model);
        EditText year = (EditText) findViewById(R.id.year);
        TextView carInfo = (TextView) findViewById(R.id.currentCar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starts a new drive
                openNewActivity();
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Car car = new Car(make.getText().toString(),
                        model.getText().toString(), year.getText().toString(), getApplicationContext());
                make.setText("");
                model.setText("");
                year.setText("");
                carInfo.setVisibility(View.VISIBLE);
                //TODO - Work on having this action wait until API response received


                carInfo.setText(car.toString());
            }
        });

        //Bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;

                    case R.id.home:
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
     * Opens new activity to start drive
     */
    private void openNewActivity() {
        Intent intent = new Intent(this, StartDriveActivity.class);
        startActivity(intent);
    }
}