package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortByWeightHigh implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (b.getWeight() - a.getWeight());
    }
}
