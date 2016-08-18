package com.nikitakozlov.pury.internal;

import java.util.List;

public class Stage {
    private final String mName;
    private final int mOrder;
    private final StopWatch mStopWatch;
    private boolean mIsStarted;
    private boolean mIsStopped;
    private Stage mActiveNestedStage;
    private List<Stage> mStages;

    public Stage(String name, int order) {
        this(name, order, new StopWatch());
    }

    Stage(String name, int order, StopWatch stopWatch) {
        mName = name;
        mOrder = order;
        mStopWatch = stopWatch;
    }

    public String getName() {
        return mName;
    }

    public int getOrder() {
        return mOrder;
    }


    public void start() {
        if (!mIsStarted && !mIsStopped) {
            mStopWatch.start();
            mIsStarted = true;
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
                mIsStarted = false;
            }
        } else if (!mActiveNestedStage.mIsStopped) {
            mActiveNestedStage.stop(stageName);
        }
    }

    public boolean isStopped() {
        return mIsStopped;
    }


    public long getExecTimeInMillis() {
        return mStopWatch.getExecTimeInMillis();
    }

    public List<Stage> getStages() {
        return mStages;
    }
}
