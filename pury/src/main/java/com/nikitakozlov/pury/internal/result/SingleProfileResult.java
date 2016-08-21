package com.nikitakozlov.pury.internal.result;

import java.util.List;

public class SingleProfileResult implements ProfileResult {

    private static final String DEPTH_PREFIX = "  ";

    private final String mStageName;
    private final List<SingleProfileResult> mNestedResults;
    private final long mStartTime;
    private final long mExecTime;
    private final int mDepth;

    public SingleProfileResult(String mStageName, long startTime, long execTime,
                               List<SingleProfileResult> nestedResults, int depth) {
        this.mStageName = mStageName;
        mNestedResults = nestedResults;
        mStartTime = startTime;
        mExecTime = execTime;
        mDepth = depth;
    }

    @Override
    public List<? extends ProfileResult> getNestedResults() {
        return mNestedResults;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getExecTime() {
        return mExecTime;
    }

    protected String getStageName() {
        return mStageName;
    }

    @Override
    public String toString() {

        String depthPrefix = getFullDepthPrefix();

        StringBuilder stringBuilder = new StringBuilder(depthPrefix);
        stringBuilder.append(mStageName);
        stringBuilder.append(" --> ");
        stringBuilder.append(getStartTime());
        stringBuilder.append("ms \n");
        for (ProfileResult result : getNestedResults()) {
            stringBuilder.append(depthPrefix);
            stringBuilder.append(result.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append(depthPrefix);
        stringBuilder.append(mStageName);
        stringBuilder.append(" <-- ");
        stringBuilder.append(getExecTime());
        stringBuilder.append("ms");

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
