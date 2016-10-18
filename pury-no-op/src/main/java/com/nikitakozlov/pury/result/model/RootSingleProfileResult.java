package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

/**
 * Represent result for a top most stage. Start time is always zero.
 */
public class RootSingleProfileResult extends SingleProfileResult {

    public RootSingleProfileResult(String stageName, long execTime, List<SingleProfileResult> nestedResults) {
        super(stageName, 0L, execTime, nestedResults, 0);
    }

    @Override
    public void accept(ResultVisitor visitor) {
    }
}
