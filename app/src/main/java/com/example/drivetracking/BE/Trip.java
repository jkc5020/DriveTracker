package com.example.drivetracking.BE;

public class Trip {
    private Double miles;
    private String time;
    private int gallons;



    public Trip(Double miles, String time) {
        this.miles = miles;
        this.time = time;
        this.gallons = 0;
    }

    public int getGallons() {
        return gallons;
    }

    public void setGallons(int gallons) {
        this.gallons = gallons;
    }

    public Double getMiles() {
        return miles;
    }

    public String getTime() {
        return time;
    }
}
