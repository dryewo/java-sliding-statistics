package me.dryewo.sliding_statistics;

import java.io.Serializable;
import java.util.Comparator;

public class Item implements Serializable {
    final long timestamp;
    final double amount;

    public Item(long timestamp, double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }

    static Comparator<Item> timestampComparator = Comparator.comparingLong(x -> x.timestamp);
    static Comparator<Item> valueComparatorAsc = Comparator.comparingDouble(x -> x.amount);
    static Comparator<Item> valueComparatorDesc = valueComparatorAsc.reversed();

    @Override
    public String toString() {
        return "Item{timestamp=" + timestamp + ", amount=" + amount + '}';
    }
}
