package me.dryewo.sliding_statistics;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SlidingSumTest {
    SlidingSum slidingSum;

    @Before
    public void setUp() throws Exception {
        slidingSum = new SlidingSum(1000);
    }

    @Test
    public void shouldReturnZeroIfEmpty() throws Exception {
        assertEquals(0, slidingSum.getSum(0), 0);
        assertEquals(0, slidingSum.getCount(0));
    }

    @Test
    public void shouldReturnSum() throws Exception {
        slidingSum.add(0, new Item(0, 100));
        slidingSum.add(0, new Item(0, 100));
        assertEquals(200, slidingSum.getSum(500), 0);
        assertEquals(2, slidingSum.getCount(500));
    }

    @Test
    public void shouldDropOldItems() throws Exception {
        slidingSum.add(0, new Item(0, 100));
        slidingSum.add(0, new Item(0, 100));
        slidingSum.add(0, new Item(1000, 100));
        slidingSum.add(0, new Item(1000, 100));
        // First two added items should be dropped
        assertEquals(200, slidingSum.getSum(1500), 0);
        assertEquals(2, slidingSum.getCount(1500));
    }

    @Test
    public void shouldNotAccumulateFloatingPointError() throws Exception {
        slidingSum.add(0, new Item(0, 10.4));
        slidingSum.add(50, new Item(50, 10.4));
        slidingSum.add(500, new Item(500, 100));
        assertEquals(100, slidingSum.getSum(1200), 0);
    }

}
