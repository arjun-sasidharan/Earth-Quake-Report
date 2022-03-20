package com.example.earthquakereport;
// this is class of earth quake report
public class EarthQuake {

    private double mMagnitudeInDecimal;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    //constructor
    public EarthQuake(double vMagnitude, String vLocation, long timeInMilliseconds, String url){
        mMagnitudeInDecimal = vMagnitude;
        mLocation = vLocation;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
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
    public String getUrl(){
        return mUrl;
    }
}
