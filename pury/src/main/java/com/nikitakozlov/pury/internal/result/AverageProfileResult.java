package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class AverageProfileResult implements ProfileResult {

    private final AverageTime mStartTime;
    private final AverageTime mExecTime;

    public AverageProfileResult(AverageTime mStartTime, AverageTime mExecTime) {
        this.mStartTime = mStartTime;
        this.mExecTime = mExecTime;
    }

    public AverageTime getStartTime() {
        return mStartTime;
    }

    public AverageTime getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return null;
    }
}
