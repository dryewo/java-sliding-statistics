package me.dryewo.sliding_statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StatisticsAsync {
    private Logger log = LoggerFactory.getLogger(StatisticsAsync.class);

    private final long refreshIntervalMs;
    private final Statistics statistics;
    private volatile Result latestResult;

    public StatisticsAsync(long windowSizeMs, long refreshIntervalMs) {
        this.refreshIntervalMs = refreshIntervalMs;
        this.statistics = new Statistics(windowSizeMs);
        log.info("Starting StatisticsAsync with window size of {} ms", windowSizeMs);
        new Thread(new Consumer()).start();
        new Thread(new Refresher()).start();
    }

    public Result get() {
        // Since the latestResult is refreshed, the old transactions are purged almost up to date
        // This is strongly consistent and almost O(1)
        synchronized (statistics) {
            long now = System.currentTimeMillis();
            return statistics.getStatistics(now);
        }
        // For strict O(1) we need to agree to allow eventual consistency and always return the view
//        return latestResult;
    }

    public void add(Item item) {
        queue.add(item);
    }

    final BlockingQueue<Item> queue = new LinkedBlockingQueue<>();

    private class Consumer implements Runnable {
        public void run() {
            log.info("Consumer: started");
            while (true) {
                try {
                    Item item = queue.take();
                    log.info("Consumer: received an item: {}", item);
                    synchronized (statistics) {
                        long now = System.currentTimeMillis();
                        statistics.add(now, item);
                        // Immediately refresh
                        latestResult = statistics.getStatistics(now);
                    }
                } catch (Exception e) {
                    log.error("Consumer: error while processing the queue.", e);
                }
            }
        }
    }

    private class Refresher implements Runnable {
        public void run() {
            log.info("Refresher: started");
            while (true) {
                try {
                    Thread.sleep(refreshIntervalMs);
                    synchronized (statistics) {
                        long now = System.currentTimeMillis();
                        latestResult = statistics.getStatistics(now);
                    }
                    log.debug("Refresher: refreshed the latest result: {}", latestResult);
                } catch (Exception e) {
                    log.error("Refresher: error while refreshing the latest result.", e);
                }
            }
        }
    }
}
