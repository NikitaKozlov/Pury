package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for multiple runs for top most stage. Compare to {@link AverageProfileResult},
 * it doesn't have Start Time because it is always zero.
 */
public class RootAverageProfileResult implements ProfileResult {

    private final String mStageName;
    private final AverageTime mExecTime;
    private final List<AverageProfileResult> mAverageProfileResults;

    public RootAverageProfileResult(String mStageName, AverageTime execTime,
                                    List<AverageProfileResult> averageProfileResults) {
        this.mStageName = mStageName;
        mExecTime = execTime;
        mAverageProfileResults = averageProfileResults;
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
        return 0;
    }
}
