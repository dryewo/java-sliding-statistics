package me.dryewo.sliding_statistics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatisticsAsyncTest {

    @Test
    public void worksInGeneral() throws Exception {
        StatisticsAsync statisticsAsync = new StatisticsAsync(500, 100);
        // Ugly and flaky test, disable if fails too often
        long now = System.currentTimeMillis();
        statisticsAsync.add(new Item(now, 0));
        statisticsAsync.add(new Item(now, 100));
        Thread.sleep(50);
        assertEquals(new Result(0., 100., 100., 50., 2), statisticsAsync.get());
        Thread.sleep(200);
        assertEquals(new Result(0., 100., 100., 50., 2), statisticsAsync.get());
        Thread.sleep(500);
        assertEquals(new Result(null, null, 0., 0., 0), statisticsAsync.get());
    }

    static void insertNTransactions(StatisticsAsync statisticsAsync, int N) {
        for (int i = 0; i < N; ++i) {
            statisticsAsync.add(new Item(System.currentTimeMillis(), 1));
        }
    }

    @Test
    public void loadTest() throws Exception {
        StatisticsAsync statisticsAsync = new StatisticsAsync(10000, 10);

        // Insert N transactions in each of T threads
        final int T = 100;
        final int N = 1000;
        Thread[] threads = new Thread[T];
        for (int i = 0; i < T; ++i) {
            threads[i] = new Thread(() -> insertNTransactions(statisticsAsync, N));
            threads[i].run();
        }

        // Wait for the transactions to be processed
        for (int i = 0; i < T; ++i)
            threads[i].join();
        while (statisticsAsync.queue.size() > 0) {
            Thread.sleep(100);
        }

        assertEquals(new Result(1., 1., T * N * 1., 1., T * N), statisticsAsync.get());
    }
}
