package com.example.rhys.geodash;

import java.util.ArrayList;

/**
 * Created by rhys on 02/04/16.
 */
public class MapModel {
    private ArrayList<RiddleLocation> mRiddleLocation;
    private String mMapName;
    private int mTimeLimit;
    private int mNumRiddles;

    public MapModel(String name, int timeLimit, int numRiddles)
    {
        mMapName = name;
        mTimeLimit = timeLimit;
        mNumRiddles = numRiddles;

        mRiddleLocation = new ArrayList<RiddleLocation>();
    }

    public ArrayList<RiddleLocation> getRiddleLocation()
    {
        return mRiddleLocation;
    }

    public String getMapName()
    {
        return mMapName;
    }

    public int getmTimeLimit()
    {
        return mTimeLimit;
    }

    public int getNumRiddles()
    {
        return mNumRiddles;
    }
}
