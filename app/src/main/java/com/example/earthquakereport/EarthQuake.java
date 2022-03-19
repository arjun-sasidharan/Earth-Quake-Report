package com.example.earthquakereport;
// this is class of earth quake report
public class EarthQuake {

    private String mMagnitude;
    private String mLocation;
    private String mDate;

    //constructor
    public EarthQuake(String vMagnitude, String vLocation, String vDate){
        mMagnitude = vMagnitude;
        mLocation = vLocation;
        mDate = vDate;
    }

    //method to get magnitude
    public String getMagnitude(){
        return mMagnitude;
    }

    //method to get location
    public String getLocation(){
        return mLocation;
    }
    public String getDate(){
        return mDate;
    }

}
