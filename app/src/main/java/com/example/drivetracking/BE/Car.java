package com.example.drivetracking.BE;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drivetracking.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * The car class stores the users car's year, make, and model,
 * and uses the Volley library to make an API call to the FuelEconomy.gov website
 * in order to calculate the mpg of that car
 */
public class Car {
    private String URL = ""; //Variable for API endpoint
    private String make;    //car make
    private String model;   //car model
    private String year;    //car year
    private int mpg;        //mpg
    private JsonObjectRequest jsonObjectRequest = null;
    private Context context;

    /**
     * Constructor
     * @param make - car make
     * @param model - car model
     * @param year - car year
     * @param context - application context
     */
    public Car(String make, String model, String year, int mpg, Context context) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.mpg = mpg;
        this.URL = "https://www.fueleconomy.gov/ws/rest/ympg/shared/vehicles?make=" + make + "&model="+ model + "&year=" + year;
        this.context = context;
        //makeAPICall(context);
        //callTwo(context);
        //callThree(context);
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public int getMpg() {
        return mpg;
    }

    /**
     * older method used for testing API, not currently used
     * but here for reference
     * @param context - application context
     */
    private void makeAPICall(Context context){


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response.getJSONObject(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap();
                params.put("make", make);
                params.put("model", model);
                params.put("year", year);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap <String, String> headers = new HashMap <String, String>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Accept-Encoding", "gzip, deflate, br");
                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Current method used to call API endpoint URL. Uses the Android volley library
     * to retrieve the JsonArray from the returned JsonObject, and calls helper method
     * to parse the response to locate the mpg
     * @param context
     */
    private void callTwo(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        this.jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

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
        public Map getHeaders() throws AuthFailureError{
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
            if (jsonArray.getJSONObject(i).getString("year").equals(this.year)) {
                System.out.println(jsonArray.getJSONObject(i).getString("year"));
                int city = Integer.parseInt(jsonArray.getJSONObject(i).getString("city08"));
                int highway = Integer.parseInt(jsonArray.getJSONObject(i).getString("highway08"));
                this.mpg = (city + highway) / 2;
                System.out.println(this.mpg);
            }

        }
    }

    /**
     * Not used
     * @param context - context
     */
    private void callThree(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    System.out.println(respObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String>  params = new HashMap <String, String>();
                params.put("make", make);
                params.put("model", model);
                params.put("year", year);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap <String, String> headers = new HashMap <String, String>();
                headers.put("Cache-Control", "no-cache");
                headers.put("Accept-Encoding", "gzip, deflate, br");
                headers.put("Connection", "keep-alive");
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        requestQueue.add(stringRequest);

    }

    @NonNull
    @Override
    public String toString() {

        return("Your Car is a " + this.year + " " + this.make + " " + this.model + ", and its mpg is " + this.mpg);
    }
}



