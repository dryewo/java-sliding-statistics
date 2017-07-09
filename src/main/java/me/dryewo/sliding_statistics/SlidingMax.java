package me.dryewo.sliding_statistics;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

// Not thread safe, needs synchronization
public class SlidingMax {
    final long windowSize;
    final Comparator<Item> comparator;
    // The window is always sorted by timestamp
    // We also make sure it's sorted by amount
    // We don't insert items that have no chance of becoming maximal
    //  and remove offending items after inserting a new one
    final TreeSet<Item> window = new TreeSet<>(Item.timestampComparator);

    public SlidingMax(long windowSize, Comparator<Item> comparator) {
        this.comparator = comparator;
        this.windowSize = windowSize;
    }

    public void add(long now, Item item) {
        if (!window.isEmpty()) {
            Item nextLaterItem = window.ceiling(item); // same or later timestamp
            // If there would be a bigger item later than the new one, don't insert
            if (nextLaterItem != null && lessThan(item, nextLaterItem)) {
                // don't insert
            } else {
                // Delete all smaller items that are earlier than the new one
                // TODO removeIf can be optimized to removeWhile, as it's sorted
                window.headSet(item, true).removeIf(i -> lessThan(i, item));
                window.add(item);
            }
        } else {
            window.add(item);
        }
        purgeOldItems(now);
        // Invariant: the newest item should be smaller than the oldest (current maximum)
        assert (window.isEmpty() || !lessThan(window.first(), window.last()));
    }

    public Double get(long now) {
        purgeOldItems(now);
        // The maximum item is always kept in the head
        return window.isEmpty() ? null : window.first().amount;
    }

    private void purgeOldItems(long now) {
        while (!window.isEmpty() && window.first().timestamp + windowSize < now)
            window.pollFirst();
    }

    boolean lessThan(Item i1, Item i2) {
        return comparator.compare(i1, i2) < 0;
    }

}
