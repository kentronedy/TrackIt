package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortByDateLow implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (a.getDate().getTime() - b.getDate().getTime());
    }
}
