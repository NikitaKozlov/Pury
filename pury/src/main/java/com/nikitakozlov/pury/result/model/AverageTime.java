package com.nikitakozlov.pury.result.model;

import java.util.Locale;

public class AverageTime {

    private static final int MS_TO_NS = 1000000;

    private static final String MS = "ms";
    private static final String AVERAGE = "avg = ";
    private static final String MIN = "min = ";
    private static final String MAX = "max = ";
    private static final String FOR = "for ";
    private static final String RUNS = " runs";

    private final double mAverageValue;
    private final long mMinValue;
    private final long mMaxValue;
    private final int mRuns;

    public AverageTime(double averageValue, long minValue, long maxValue, int runs) {
        mAverageValue = averageValue;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mRuns = runs;
    }

    public double getAverageValue() {
        return mAverageValue;
    }

    public long getMinValue() {
        return mMinValue;
    }

    public long getMaxValue() {
        return mMaxValue;
    }

    public int getRuns() {
        return mRuns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AverageTime that = (AverageTime) o;

        if (Double.compare(that.mAverageValue, mAverageValue) != 0) return false;
        if (mMinValue != that.mMinValue) return false;
        if (mMaxValue != that.mMaxValue) return false;
        return mRuns == that.mRuns;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(mAverageValue);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (mMinValue ^ (mMinValue >>> 32));
        result = 31 * result + (int) (mMaxValue ^ (mMaxValue >>> 32));
        result = 31 * result + mRuns;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AVERAGE);
        stringBuilder.append(String.format(Locale.US, "%.2f", mAverageValue / MS_TO_NS));
        stringBuilder.append(MS);
        stringBuilder.append(", ");

        stringBuilder.append(MIN);
        stringBuilder.append(mMinValue / MS_TO_NS);
        stringBuilder.append(MS);
        stringBuilder.append(", ");

        stringBuilder.append(MAX);
        stringBuilder.append(mMaxValue / MS_TO_NS);
        stringBuilder.append(MS);
        stringBuilder.append(", ");

        stringBuilder.append(FOR);
        stringBuilder.append(mRuns);
        stringBuilder.append(RUNS);

        return stringBuilder.toString();
    }
}
