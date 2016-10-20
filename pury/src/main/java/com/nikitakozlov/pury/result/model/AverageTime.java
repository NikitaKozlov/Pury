package com.nikitakozlov.pury.result.model;

/**
 * Represents average time for given amount of measurements.
 */
public class AverageTime {


    private final double mAverageValue;
    private final long mMinValue;
    private final long mMaxValue;
    private final int mMeasurementCounter;

    public AverageTime(double averageValue, long minValue, long maxValue, int measurementCounter) {
        mAverageValue = averageValue;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mMeasurementCounter = measurementCounter;
    }

    /**
      * @return average time.
     */
    public double getAverageValue() {
        return mAverageValue;
    }
    /**
     * @return min time from all measurements.
     */
    public long getMinValue() {
        return mMinValue;
    }

    /**
     * @return max time from all measurements.
     */
    public long getMaxValue() {
        return mMaxValue;
    }

    /**
     * @return amount of measurements.
     */
    public int getMeasurementCounter() {
        return mMeasurementCounter;
    }
}
