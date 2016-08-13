package com.nikitakozlov.pury.internal;

import java.util.Locale;

public class MethodProfileResult {
    private final String methodId;
    private final int runsCounter;
    private final double averageValue;
    private final long minValue;
    private final long maxValue;

    public MethodProfileResult(String methodId, int runsCounter, double averageValue, long minValue, long maxValue) {
        this.methodId = methodId;
        this.runsCounter = runsCounter;
        this.averageValue = averageValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodProfileResult that = (MethodProfileResult) o;

        if (runsCounter != that.runsCounter) return false;
        if (Double.compare(that.averageValue, averageValue) != 0) return false;
        if (minValue != that.minValue) return false;
        if (maxValue != that.maxValue) return false;
        return methodId != null ? methodId.equals(that.methodId) : that.methodId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = methodId != null ? methodId.hashCode() : 0;
        result = 31 * result + runsCounter;
        temp = Double.doubleToLongBits(averageValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (minValue ^ (minValue >>> 32));
        result = 31 * result + (int) (maxValue ^ (maxValue >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return methodId + '\'' + " " + runsCounter + " runs, [" + minValue + " ,"
                + String.format(Locale.US, "%.2f", averageValue) + " ," + maxValue + ']';
    }
}
