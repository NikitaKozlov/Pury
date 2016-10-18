package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.Collections;
import java.util.List;

/**
 * Represent result for a single stage.
 */
public class SingleProfileResult implements ProfileResult {

    public SingleProfileResult(String mStageName, long startTime, long execTime,
                               List<SingleProfileResult> nestedResults, int depth) {
    }

    /**
     * @return start time of a stage since start of the top most stage.
     */
    public long getStartTime() {
        return 0;
    }

    /**
     * @return execution time of a stage.
     */
    public long getExecTime() {
        return 0;
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
