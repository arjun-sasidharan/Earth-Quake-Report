package com.example.earthquakereport;
// this is class of earth quake report
public class EarthQuake {

    private double mMagnitudeInDecimal;
    private String mLocation;
    private long mTimeInMilliseconds;

    //constructor
    public EarthQuake(double vMagnitude, String vLocation, long timeInMilliseconds){
        mMagnitudeInDecimal = vMagnitude;
        mLocation = vLocation;
        mTimeInMilliseconds = timeInMilliseconds;
    }

    //method to get magnitude
    public double getMagnitudeInDecimal(){
        return mMagnitudeInDecimal;
    }

    //method to get location
    public String getLocation(){
        return mLocation;
    }
    public long getTimeInMilliseconds(){
        return mTimeInMilliseconds;
    }

}
