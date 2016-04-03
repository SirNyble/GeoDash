package com.example.rhys.geodash;

/**
 * Created by rhys on 03/04/16.
 */
public class User {
    private int score;
    private String name;
    public User() {}
    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public long getScore() {
        return score;
    }
    public String getName() {
        return name;
    }
}

