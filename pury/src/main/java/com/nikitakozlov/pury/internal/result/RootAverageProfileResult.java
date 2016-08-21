package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class RootAverageProfileResult implements ProfileResult {

    private final AverageTime mExecTime;
    private final List<AverageProfileResult> mAverageProfileResults;

    public RootAverageProfileResult(AverageTime execTime, List<AverageProfileResult> averageProfileResults) {
        mExecTime = execTime;
        mAverageProfileResults = averageProfileResults;
    }

    public AverageTime getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mAverageProfileResults;
    }
}
