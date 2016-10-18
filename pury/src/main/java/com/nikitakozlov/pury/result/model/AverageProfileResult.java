package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for multiple runs for a single stage.
 */
public class AverageProfileResult implements ProfileResult {

    private final String mStageName;
    private final AverageTime mStartTime;
    private final AverageTime mExecTime;
    private final List<AverageProfileResult> mAverageProfileResults;
    private final int mDepth;

    public AverageProfileResult(String stageName, AverageTime startTime, AverageTime execTime,
                                List<AverageProfileResult> averageProfileResults, int depth) {
        mStageName = stageName;
        mStartTime = startTime;
        mExecTime = execTime;
        mAverageProfileResults = averageProfileResults;
        mDepth = depth;
    }

    /**
     * @return Averaged start time since start of top most stage.
     */
    public AverageTime getStartTime() {
        return mStartTime;
    }

    /**
     * @return Averaged execution time.
     */
    public AverageTime getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mAverageProfileResults;
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


