package me.dryewo.sliding_statistics;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StatisticsTest {
    Statistics statistics;

    @Before
    public void setUp() throws Exception {
        statistics = new Statistics(1000);
    }

    @Test
    public void worksForEmpty() throws Exception {
        statistics.add(0, new Item(0, 100));
        Result result = statistics.getStatistics(1500);
        assertNull(result.min);
        assertNull(result.max);
        assertEquals(0, result.avg, 0);
        assertEquals(0, result.sum, 0);
        assertEquals(0, result.count, 0);
    }

    @Test
    public void worksInGeneral() throws Exception {
        statistics.add(0, new Item(0, 100));
        statistics.add(1000, new Item(0, 1000));
        statistics.add(1000, new Item(1000, 100));
        Result result = statistics.getStatistics(1500);
        assertEquals(100, result.min, 0);
        assertEquals(100, result.max, 0);
        assertEquals(100, result.avg, 0);
        assertEquals(100, result.sum, 0);
        assertEquals(1, result.count, 0);
    }

}
