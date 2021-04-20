package com.aloydev.weighttracker;

import java.util.Comparator;

public class SortBySleepLow implements Comparator<DataEntry> {
    @Override
    public int compare(DataEntry a, DataEntry b) {
        return (int) (a.getSleep() - b.getSleep());
    }
}
