package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortByWeightLow implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (a.getWeight() - b.getWeight());
    }
}
