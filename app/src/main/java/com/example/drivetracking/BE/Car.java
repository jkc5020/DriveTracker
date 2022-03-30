package com.example.drivetracking.BE;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Car {
    private String URL = "";
    private String make;
    private String model;
    private String year;
    private int mpg;

    public Car(String make, String model, String year, Context context) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.URL = "https://www.fueleconomy.gov/ws/rest/ympg/shared/vehicles?make=" + make + "&model="+ model + "&year=" + year;
        //makeAPICall(context);
        callTwo(context);
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

    private void callTwo(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            //System.out.println(response.toString());
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
            System.out.println(error.toString());
        }
    }){
        @Override
        protected Map getParams(){
            Map params = new HashMap();
            params.put("make", make);
            params.put("model", model);
            params.put("year", year);
            return params;
        }

        @Override
        public Map getHeaders() throws AuthFailureError{
            HashMap <String, String> headers = new HashMap <String, String>();
            headers.put("Cache-Control", "no-cache");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Connection", "keep-alive");
            headers.put("Accept", "application/json");

            return headers;
        }
    };
        requestQueue.add(jsonObjectRequest);

    }

    private void parseData(JSONArray jsonArray) throws JSONException {
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
}



