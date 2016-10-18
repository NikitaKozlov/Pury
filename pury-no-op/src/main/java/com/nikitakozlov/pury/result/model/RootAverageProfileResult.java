package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for multiple runs for top most stage. Compare to {@link AverageProfileResult},
 * it doesn't have Start Time because it is always zero.
 */
public class RootAverageProfileResult implements ProfileResult {

    public RootAverageProfileResult(String stageName, AverageTime execTime,
                                    List<AverageProfileResult> averageProfileResults) {
    }

    /**
     * @return Averaged execution time.
     */
    public AverageTime getExecTime() {
        return null;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return null;
    }

    @Override
    public void accept(ResultVisitor visitor) {
    }


    @Override
    public String getStageName() {
        return null;
    }

    @Override
    public int getDepth() {
        return 0;
    }
}
