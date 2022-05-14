package com.example.bigqueryapplication.LocationAnalysis;

import java.io.Serializable;

public class Location implements Serializable {

    private String countryName;
    private int count;

    public Location() {
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
