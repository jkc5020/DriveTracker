package com.example.drivetracking.BE;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilderFactory;

public class Trip implements Parcelable {
    public Double miles;
    public String time;
    private int gallons;
    private int totalSeconds;


    public Trip(Double miles, String time, int totalSeconds) {
        this.miles = miles;
        this.time = time;
        this.gallons = 0;
        this.totalSeconds = totalSeconds;
    }



    protected Trip(Parcel in) {
        if (in.readByte() == 0) {
            miles = null;
        } else {
            miles = in.readDouble();
        }
        time = in.readString();
        gallons = in.readInt();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public int getTotalSeconds() {
        return totalSeconds;
    }
    public int getGallons() {
        return gallons;
    }

    public void setGallons(int gallons) {
        this.gallons = gallons;
    }

    public Double getMiles() {
        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        return Double.valueOf(decimalFormat.format(miles));
    }

    public String getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(miles);
        dest.writeString(time);
        dest.writeInt(gallons);

    }
}
