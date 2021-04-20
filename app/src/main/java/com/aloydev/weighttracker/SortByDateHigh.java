package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortByDateHigh implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (b.getDate().getTime() - a.getDate().getTime());
    }
}
