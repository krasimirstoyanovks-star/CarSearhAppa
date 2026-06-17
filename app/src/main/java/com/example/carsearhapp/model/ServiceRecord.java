package com.example.carsearhapp.model;

import java.io.Serializable;

public class ServiceRecord implements Serializable {
    private String date;
    private String description;
    private int mileage;

    public ServiceRecord(String date, String description, int mileage) {
        this.date = date;
        this.description = description;
        this.mileage = mileage;
    }

    public String getDate() { return date; }
    public String getDescription() { return description; }
    public int getMileage() { return mileage; }
}