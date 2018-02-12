package com.bignerdranch.android.iotproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brain Power on 2/11/2018.
 */

public class LocationPlace {
    private String name;
    private String lineName;

    private int ratio;
    private int ratioAll;
    private int numSubway;

    private double mLat;
    private double mLen;

    public LocationPlace(String name, double lat, double len) {
        this.name = name;
        mLat = lat;
        mLen = len;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLen() {
        return mLen;
    }

    public void setLen(double len) {
        mLen = len;
    }

    public int getNumSubway() {
        return numSubway;
    }

    public void setNumSubway(int numSubway) {
        this.numSubway = numSubway;
    }

    public List<String> getInfor(){
        List<String>  infor = new ArrayList<>();

        infor.add(name);
        infor.add(lineName);
        infor.add(mLat + "");
        infor.add(mLen + "");
        infor.add(ratio + "");
        infor.add(ratioAll + "");
        infor.add(numSubway + "");

        return infor;
    }
}
