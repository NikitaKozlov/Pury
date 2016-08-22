package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class AverageProfileResult implements ProfileResult {

    private static final String DEPTH_PREFIX = "  ";

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

    public AverageTime getStartTime() {
        return mStartTime;
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

        String depthPrefix = getFullDepthPrefix();

        StringBuilder stringBuilder = new StringBuilder(depthPrefix);
        stringBuilder.append(mStageName);
        stringBuilder.append(" --> ");
        stringBuilder.append(getStartTime());
        stringBuilder.append("\n");
        for (ProfileResult result : getNestedResults()) {
            stringBuilder.append(result);
            stringBuilder.append("\n");
        }
        stringBuilder.append(depthPrefix);
        stringBuilder.append(mStageName);
        stringBuilder.append(" <-- ");
        stringBuilder.append(getExecTime());

        return  stringBuilder.toString();
    }

    private final String getFullDepthPrefix() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mDepth; i++) {
            stringBuilder.append(DEPTH_PREFIX);
        }
        return stringBuilder.toString();
    }
}


