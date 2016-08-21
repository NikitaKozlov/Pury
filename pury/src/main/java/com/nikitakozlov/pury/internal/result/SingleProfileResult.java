package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class SingleProfileResult implements ProfileResult {

    private final List<SingleProfileResult> mNestedResults;
    private final long mStartTime;
    private final long mExecTime;

    public SingleProfileResult(long startTime, long execTime, List<SingleProfileResult> nestedResults) {
        mNestedResults = nestedResults;
        mStartTime = startTime;
        mExecTime = execTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mNestedResults;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getExecTime() {
        return mExecTime;
    }
}
