package com.example.rhys.geodash;

/**
 * Created by rhys on 23/02/16.
 */
public class RiddleLocation {
    private double mLatitude;
    private double mLongitude;
    private String mName;
    private String mRiddle;
    public RiddleLocation()
    {

    }

    public double getLatitude()
    {
        return mLatitude;
    }
    public void setLatitude(double val)
    {
        mLatitude = val;
    }

    public double getLongitude()
    {
        return mLongitude;
    }
    public void setLongitude(double val)
    {
        mLongitude = val;
    }

    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }

    public String getRiddle()
    {
        return mRiddle;
    }
    public void setRiddle(String riddle)
    {
        mRiddle = riddle;
    }
    public String toString()
    {
        return "Lat: " + mLatitude + " Long: " + mLongitude
                + ". Name: " + mName + ", and riddle: " + mRiddle;

    }
}
