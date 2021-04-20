package com.aloydev.weighttracker;

import java.util.Date;

public class DataEntry {

    private Date date;
    private double weight;
    private double sleep;

    public DataEntry(Date date, double weight, double sleep) {
        this.date = date;
        this.weight = weight;
        this.sleep = sleep;
    }

    public Date getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public double getSleep() {
        return sleep;
    }
}
