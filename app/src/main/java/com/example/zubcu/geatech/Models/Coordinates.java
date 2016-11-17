package com.example.zubcu.geatech.Models;

/**
 * Created by user on 11/17/2016.
 */

public class Coordinates
{
    public static final Coordinates UNDEFINED = null;
    double mLatitude;
    double mLongitude;

    public Coordinates(double latitude, double longitude)
    {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }
}
