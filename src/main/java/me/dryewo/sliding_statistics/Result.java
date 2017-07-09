package me.dryewo.sliding_statistics;

class Result {
    final Double min;
    final Double max;
    final Double sum;
    final Double avg;
    final int count;

    public Result(Double min, Double max, Double sum, Double avg, int count) {
        this.min = min;
        this.max = max;
        this.sum = sum;
        this.avg = avg;
        this.count = count;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Result{min=" + min + ", max=" + max + ", sum=" + sum + ", avg=" + avg + ", count=" + count + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (count != result.count) return false;
        if (min != null ? !min.equals(result.min) : result.min != null) return false;
        if (max != null ? !max.equals(result.max) : result.max != null) return false;
        if (sum != null ? !sum.equals(result.sum) : result.sum != null) return false;
        return avg != null ? avg.equals(result.avg) : result.avg == null;
    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (sum != null ? sum.hashCode() : 0);
        result = 31 * result + (avg != null ? avg.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }
}
