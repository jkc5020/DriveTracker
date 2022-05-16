package com.example.drivetracking.BE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.NonNull;



/**
 * The car class stores the users car's year, make, and model,
 * and uses the Volley library to make an API call to the FuelEconomy.gov website
 * in order to calculate the mpg of that car
 */
public class Car implements Parcelable {
    private String make;    //car make
    private String model;   //car model
    private String year;    //car year
    private int mpg;        //mpg
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
        this.context = context;

    }

    protected Car(Parcel in) {

        make = in.readString();
        model = in.readString();
        year = in.readString();
        mpg = in.readInt();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    /**
     * Returns the make
     * @return - the make of the car
     */
    public String getMake() {
        return make;
    }

    /**
     * Returns the model
     * @return - the model of the car
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the year
     * @return - the year of the car
     */
    public String getYear() {
        return year;
    }

    /**
     * Returns the mpg
     * @return - the mpg of the car
     */
    public int getMpg() {
        return mpg;
    }




    @NonNull
    @Override
    public String toString() {

        return("Your Car is a " + this.year + " " + this.make + " " + this.model + ", and its mpg is " + this.mpg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.make);
        dest.writeString(this.model);
        dest.writeString(this.year);
        dest.writeInt(this.mpg);

    }
}



