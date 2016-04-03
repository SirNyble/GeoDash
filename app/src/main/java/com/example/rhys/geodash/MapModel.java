package com.example.rhys.geodash;

import java.util.ArrayList;

/**
 * Created by rhys on 02/04/16.
 */
public class MapModel {
    private ArrayList<RiddleLocation> riddleLocation;
    private String mapName;
    private int timeLimit;
    private int numRiddles;

    public MapModel()
    {

    }

    public MapModel(String name, int timeLimitIn, int numRiddlesIn)
    {
        mapName = name;
        timeLimit = timeLimitIn;
        numRiddles = numRiddlesIn;

        riddleLocation = new ArrayList<RiddleLocation>();
    }

    public MapModel(String name, int timeLimitIn, int numRiddlesIn,ArrayList<RiddleLocation> riddleLocationIn )
    {
        mapName = name;
        timeLimit = timeLimit;
        numRiddles = numRiddles;
        riddleLocation = riddleLocationIn;
    }

    public ArrayList<RiddleLocation> getRiddleLocation()
    {
        return riddleLocation;
    }

    public String getMapName()
    {
        return mapName;
    }

    public int getTimeLimit()
    {
        return timeLimit;
    }

    public int getNumRiddles()
    {
        return numRiddles;
    }

    public String toString()
    {
        return "MapName: " + mapName + ", numRiddles: " + numRiddles + ", timeLimit: " + timeLimit + ", RiddleLocation: " + riddleLocation.toString();
    }
}
