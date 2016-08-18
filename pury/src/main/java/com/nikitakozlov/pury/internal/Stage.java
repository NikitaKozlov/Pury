package com.nikitakozlov.pury.internal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Stage {
    private final String mName;
    private final int mOrder;
    private final StopWatch mStopWatch;
    private boolean mIsStarted;
    private boolean mIsStopped;
    private Stage mActiveNestedStage;
    private final List<Stage> mStages;

    public Stage(String name, int order) {
        this(name, order, new StopWatch());
    }

    Stage(String name, int order, StopWatch stopWatch) {
        mName = name;
        mOrder = order;
        mStopWatch = stopWatch;
        mStages = new CopyOnWriteArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public int getOrder() {
        return mOrder;
    }


    /**
     * Start this stage
     * @return true if start was successful
     */
    public boolean start() {
        if (!mIsStarted && !mIsStopped) {
            mStopWatch.start();
            mIsStarted = true;
            return true;
        }
        return false;
    }

    public boolean startStage(String stageName, int stageOrder) {
        if (mOrder >= stageOrder || !mIsStarted) {
            return false;
        }

        if (mActiveNestedStage == null || mActiveNestedStage.mIsStopped) {
            startNewNestedStage(stageName, stageOrder);
            return true;
        } else {
            if (mActiveNestedStage.getOrder() < stageOrder) {
                return mActiveNestedStage.startStage(stageName, stageOrder);
            }
            return false;
        }
    }

    private void startNewNestedStage(String stageName, int stageOrder) {
        Stage stage = new Stage(stageName, stageOrder);
        stage.start();
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

    boolean isStarted() {
        return mIsStarted;
    }

    public long getExecTimeInMillis() {
        return mStopWatch.getExecTimeInMillis();
    }

    public List<Stage> getStages() {
        return mStages;
    }
}
