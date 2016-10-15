package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

public class SingleProfileResult implements ProfileResult {

    private final String mStageName;
    private final List<SingleProfileResult> mNestedResults;
    private final long mStartTime;
    private final long mExecTime;
    private final int mDepth;

    public SingleProfileResult(String mStageName, long startTime, long execTime,
                               List<SingleProfileResult> nestedResults, int depth) {
        this.mStageName = mStageName;
        mNestedResults = nestedResults;
        mStartTime = startTime;
        mExecTime = execTime;
        mDepth = depth;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mNestedResults;
    }

    @Override
    public void accept(ResultVisitor visitor) {
        visitor.visit(this);
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getExecTime() {
        return mExecTime;
    }

    public String getStageName() {
        return mStageName;
    }

    @Override
    public int getDepth() {
        return mDepth;
    }
}
