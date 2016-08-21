package com.nikitakozlov.pury.internal.result;

import java.util.List;

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

    public AverageTime getExecTime() {
        return mExecTime;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mAverageProfileResults;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(mStageName);
        stringBuilder.append(" --> 0ms \n");
        for (ProfileResult result : getNestedResults()) {
            stringBuilder.append(result.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append(mStageName);
        stringBuilder.append(" <-- ");
        stringBuilder.append(mExecTime.toString());

        return  stringBuilder.toString();
    }
}
