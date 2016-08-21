package com.nikitakozlov.pury.internal.result;

public class AverageTime {
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
}
