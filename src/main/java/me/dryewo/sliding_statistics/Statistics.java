package me.dryewo.sliding_statistics;

public class Statistics {

    final SlidingMax slidingMax;
    final SlidingMax slidingMin;
    final SlidingSum slidingSum;

    public Statistics(long windowSize) {
        this.slidingMax = new SlidingMax(windowSize, Item.valueComparatorAsc);
        this.slidingMin = new SlidingMax(windowSize, Item.valueComparatorDesc);
        this.slidingSum = new SlidingSum(windowSize);
    }

    public void add(long now, Item item) {
        slidingMax.add(now, item);
        slidingMin.add(now, item);
        slidingSum.add(now, item);
    }

    public Result getStatistics(long now) {
        double sum = slidingSum.getSum(now);
        int count = slidingSum.getCount(now);
        double avg = count > 0 ? sum / count : 0;
        return new Result(
                slidingMin.get(now),
                slidingMax.get(now),
                sum,
                avg,
                count
        );
    }
}
