package me.dryewo.sliding_statistics;

import java.math.BigDecimal;
import java.util.PriorityQueue;

// Not thread safe, needs synchronization
public class SlidingSum {
    final long windowSize;
    final PriorityQueue<Item> window = new PriorityQueue<>(Item.timestampComparator);
    // BigDecimal is necessary to avoid floating point error (getting 99.999999999999 instead of 100.0 at some point)
    private BigDecimal sum = BigDecimal.ZERO;

    public SlidingSum(long windowSize) {
        this.windowSize = windowSize;
    }

    public void add(long now, Item item) {
        window.add(item);
        sum = sum.add(BigDecimal.valueOf(item.amount));
        purgeOldItems(now);
    }

    public double getSum(long now) {
        purgeOldItems(now);
        return sum.doubleValue();
    }

    public int getCount(long now) {
        purgeOldItems(now);
        return window.size();
    }

    private void purgeOldItems(long now) {
        while (!window.isEmpty() && window.peek().timestamp + windowSize < now) {
            Item item = window.poll();
            sum = sum.subtract(BigDecimal.valueOf(item.amount));
        }
    }
}
