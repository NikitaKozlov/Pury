package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class AverageProfileResult implements ProfileResult {

    private final AverageTime mStartTime;
    private final AverageTime mExecTime;
    private final List<AverageProfileResult> mAverageProfileResults;

    public AverageProfileResult(AverageTime startTime, AverageTime execTime,
                                List<AverageProfileResult> averageProfileResults) {
        mStartTime = startTime;
        mExecTime = execTime;
        mAverageProfileResults = averageProfileResults;
    }

    public AverageTime getStartTime() {
        return mStartTime;
    }

    public AverageTime getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mAverageProfileResults;
    }
}
