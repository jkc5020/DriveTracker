package com.example.drivetracking.BE;
import android.content.Context;
import androidx.annotation.NonNull;
import com.android.volley.toolbox.JsonObjectRequest;


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

    @NonNull
    @Override
    public String toString() {

        return(this.make + " " + this.model + " " + this.year+ ", " + this.mpg + "mpg");
    }
}



