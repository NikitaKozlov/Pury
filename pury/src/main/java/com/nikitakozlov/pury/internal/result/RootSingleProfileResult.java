package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class RootSingleProfileResult extends SingleProfileResult {

    public RootSingleProfileResult(long execTime, List<SingleProfileResult> nestedResults) {
        super(0L, execTime, nestedResults);
    }
}
