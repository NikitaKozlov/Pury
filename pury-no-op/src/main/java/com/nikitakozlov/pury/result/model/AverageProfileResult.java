package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for multiple runs for a single stage.
 */
public class AverageProfileResult implements ProfileResult {

    public AverageProfileResult(String stageName, AverageTime startTime, AverageTime execTime,
                                List<AverageProfileResult> averageProfileResults, int depth) {
    }

    /**
     * @return Averaged start time since start of top most stage.
     */
    public AverageTime getStartTime() {
        return null;
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


