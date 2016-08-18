package com.nikitakozlov.pury.internal;

import java.util.List;

public class Stage {
    private final String mName;
    private final int mOrder;
    private final StopWatch mStopWatch;
    private boolean mIsStopped;
    private Stage mActiveNestedStage;
    private List<Stage> mStages;

    public Stage(String name, int order) {
        mName = name;
        mOrder = order;
        mStopWatch = new StopWatch();
    }

    public String getName() {
        return mName;
    }

    public int getOrder() {
        return mOrder;
    }

    public long getExecTimeInMillis() {
        return mStopWatch.getExecTimeInMillis();
    }

    public void start() {
        if (!mIsStopped) {
            mStopWatch.start();
        }
    }

    public void startStage(String stageName, int stageOrder) {
        if (mOrder > stageOrder) {
            return;
        }

        if (mActiveNestedStage.mIsStopped) {
            startNewNestedStage(stageName, stageOrder);
        } else {
            if (mActiveNestedStage.getOrder() < stageOrder) {
                mActiveNestedStage.startStage(stageName, stageOrder);
            }
        }
    }

    private void startNewNestedStage(String stageName, int stageOrder) {
        Stage stage = new Stage(stageName, stageOrder);
        mActiveNestedStage = stage;
        mStages.add(stage);
    }

    public void stop(String stageName) {
        if (mName.equals(stageName)) {
            if (!mIsStopped) {
                mStopWatch.stop();
                mIsStopped = true;
            }
        } else if (!mActiveNestedStage.mIsStopped) {
            mActiveNestedStage.stop(stageName);
        }
    }

    public boolean isStopped() {
        return mIsStopped;
    }
}
