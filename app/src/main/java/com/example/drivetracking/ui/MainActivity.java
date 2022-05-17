package com.example.drivetracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.drivetracking.BE.Car;
import com.example.drivetracking.R;
import com.example.drivetracking.ui.Util.SpinnerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MainActivity shows a button to add a car along with EditText Views
 * to enter that information, as well as a button to start a drive
 */
public class MainActivity extends AppCompatActivity {
    private String URL = "";
    private Button button;
    private Button addCar;
    private EditText make;
    private EditText year;
    private EditText model;
    private Spinner carList;
    private SpinnerAdapter adapter;
    private Car car;
    private TextView carInfo;
    private String SHAREDPREFS = "sharedPrefs";
    private ArrayList<Car> cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();

        this.carList = (Spinner) findViewById(R.id.carList);
        this.carList.setAdapter(adapter);
        this.cars = new ArrayList<>();
        this.adapter = new SpinnerAdapter(MainActivity.this, android.R.layout.simple_spinner_item, cars);
        loadData();

        this.button = (Button) findViewById(R.id.startDrive);
        this.addCar = (Button) findViewById(R.id.addCar);
        this.make = (EditText) findViewById(R.id.make);
        this.model = (EditText) findViewById(R.id.model);
        this.year = (EditText) findViewById(R.id.year);
        carInfo = (TextView) findViewById(R.id.currentCar);


        if(cars.size() > 0){
            updateList();
        }
        carList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Car car = adapter.getItem(position);
                // do something
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


                //carInfo.setVisibility(View.VISIBLE);
                //TODO - Work on having this action wait until API response received
                callTwo(make.getText().toString(), model.getText().toString(), year.getText().toString());


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

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cars", null);
        Type type = new TypeToken<ArrayList<Car>>(){}.getType();
        cars = gson.fromJson(json, type);
        if(cars == null){
            cars = new ArrayList<>();
        }





    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cars);
        editor.putString("cars", json);
        editor.apply();
    }

    /**
     * Current method used to call API endpoint URL. Uses the Android volley library
     * to retrieve the JsonArray from the returned JsonObject, and calls helper method
     * to parse the response to locate the mpg
     *
     */
    private void callTwo(String make, String model, String year){
        this.URL = "https://www.fueleconomy.gov/ws/rest/ympg/shared/vehicles?make=" + make + "&model="+ model + "&year=" + year;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // get response
                //TODO - figure out way to pause program to wait for response to come in
                try {
                    JSONArray jsonArray = response.getJSONArray("vehicle");
                    parseData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handles potential error
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map getParams(){
                //not actually called with get response, had to use string concatenation
                Map params = new HashMap();
                params.put("make", make);
                params.put("model", model);
                params.put("year", year);
                return params;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                //add headers
                HashMap <String, String> headers = new HashMap <String, String>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Accept-Encoding", "gzip, deflate, br");
                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");

                return headers;
            }
        };
        //add request to the queue
        requestQueue.add(jsonObjectRequest);

    }


    /**
     * Helper method to parse the jsonArray to find the mpg of the desired car
     * Calculates avg mpg based on city and highway
     * @param jsonArray - the json array
     * @throws JSONException - exception
     */
    private void parseData(JSONArray jsonArray) throws JSONException {
        //TODO- Fine tune method used to fine avg since there can be multiple cars
        // of same year, make, and model
        // but different specs
        for(int i = 0; i < jsonArray.length(); i ++){
            if (jsonArray.getJSONObject(i).getString("year").equals(this.year.getText().toString())) {
                System.out.println(jsonArray.getJSONObject(i).getString("year"));
                int city = Integer.parseInt(jsonArray.getJSONObject(i).getString("city08"));
                int highway = Integer.parseInt(jsonArray.getJSONObject(i).getString("highway08"));
                int mpg = (city + highway) / 2;
                this.car= new Car(make.getText().toString(),
                        model.getText().toString(), year.getText().toString(), mpg);
                System.out.println(mpg);
                this.carInfo.setText(car.toString());
                make.setText("");
                model.setText("");
                year.setText("");
                cars.add(car);
                updateList();
                Toast.makeText(getApplicationContext(), "Car added", Toast.LENGTH_LONG).show();

            }

        }
    }

    private void updateList() {
        if(cars.size() > 1) {
            adapter.clear();
        }
        adapter.addAll(cars);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        carList.setAdapter(adapter);
    }

    /**
     * Opens new activity to start drive
     */
    private void openNewActivity() {
        Intent intent = new Intent(this, StartDriveActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }
}