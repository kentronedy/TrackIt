package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortBySleepHigh implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (b.getSleep() - a.getSleep());
    }
}
