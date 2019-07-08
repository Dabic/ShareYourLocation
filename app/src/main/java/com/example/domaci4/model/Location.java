package com.example.domaci4.model;

import com.google.firebase.database.Exclude;

public class Location {
    @Exclude
    private String mId;

    private String mName;

    private String mDate;

    private String mDesctiption;

    private double longtitude;

    private double latitude;

    public Location() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getDesctiption() {
        return mDesctiption;
    }

    public void setDesctiption(String desctiption) {
        this.mDesctiption = desctiption;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    @Exclude
    public String getId() {
        return mId;
    }

    @Exclude
    public void setId(String id) {
        this.mId = id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }
}
