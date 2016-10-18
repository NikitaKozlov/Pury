package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for a single stage.
 */
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

    /**
     * @return start time of a stage since start of the top most stage.
     */
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * @return execution time of a stage.
     */
    public long getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mNestedResults;
    }

    @Override
    public void accept(ResultVisitor visitor) {
        visitor.visit(this);
    }
    @Override
    public String getStageName() {
        return mStageName;
    }

    @Override
    public int getDepth() {
        return mDepth;
    }
}
