package com.nikitakozlov.pury.result.model;

import com.nikitakozlov.pury.result.ResultVisitor;

import java.util.List;

public class RootSingleProfileResult extends SingleProfileResult {

    public RootSingleProfileResult(String stageName, long execTime, List<SingleProfileResult> nestedResults) {
        super(stageName, 0L, execTime, nestedResults, 0);
    }

    @Override
    public void accept(ResultVisitor visitor) {
        visitor.visit(this);
    }
}
