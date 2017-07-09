package me.dryewo.sliding_statistics;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SlidingMaxTest {

    SlidingMax slidingMax;

    @Before
    public void setUp() {
        slidingMax = new SlidingMax(1000, Item.valueComparatorAsc);
    }

    @Test
    public void nullWhenNoItems() throws Exception {
        slidingMax.add(0, new Item(0, 42));
        assertNull(slidingMax.get(2000));
    }

    @Test
    public void oneItem() throws Exception {
        slidingMax.add(0, new Item(0, 42));
        assertEquals(42, slidingMax.get(500), 0);
    }

    @Test
    public void sameTimestamp() throws Exception {
        slidingMax.add(0, new Item(0, 50));
        slidingMax.add(0, new Item(0, 70));
        slidingMax.add(0, new Item(0, 30));
        slidingMax.add(0, new Item(0, 40));
        assertEquals(70, slidingMax.get(500), 0);
    }

    @Test
    public void discardOldItems() throws Exception {
        slidingMax.add(0, new Item(0, 200));
        slidingMax.add(0, new Item(100, 10));
        slidingMax.add(0, new Item(200, 100));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1150), 0);
    }

    @Test
    public void newerSameAmount() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(100, 100));
        assertEquals(100, slidingMax.get(1050), 0);
    }

    @Test
    public void ascending() throws Exception {
        slidingMax.add(0, new Item(0, 0));
        slidingMax.add(0, new Item(100, 100));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertNull(slidingMax.get(1150));
    }

    @Test
    public void descending() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(100, 0));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(0, slidingMax.get(1050), 0);
        assertNull(slidingMax.get(1150));
    }

    @Test
    public void mixed() throws Exception {
        slidingMax.add(0, new Item(0, 200));
        slidingMax.add(0, new Item(100, 0));
        slidingMax.add(0, new Item(200, 100));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertEquals(100, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeEarliestBiggest() throws Exception {
        slidingMax.add(0, new Item(100, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(0, 200));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeEarliestMedium() throws Exception {
        slidingMax.add(0, new Item(100, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(0, 50));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeEarliestSmallest() throws Exception {
        slidingMax.add(0, new Item(100, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(0, 0));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeMiddleBiggest() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(100, 200));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(200, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeMiddleMedium() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(100, 50));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(50, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeMiddleSmallest() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(200, 10));
        slidingMax.add(0, new Item(100, 0));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(10, slidingMax.get(1050), 0);
        assertEquals(10, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeLatestBiggest() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(100, 10));
        slidingMax.add(0, new Item(200, 200));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(200, slidingMax.get(1050), 0);
        assertEquals(200, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeLatestMedium() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(100, 10));
        slidingMax.add(0, new Item(200, 50));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(50, slidingMax.get(1050), 0);
        assertEquals(50, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }

    @Test
    public void mixedTimeLatestSmallest() throws Exception {
        slidingMax.add(0, new Item(0, 100));
        slidingMax.add(0, new Item(100, 10));
        slidingMax.add(0, new Item(200, 0));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(10, slidingMax.get(1050), 0);
        assertEquals(0, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }
}
