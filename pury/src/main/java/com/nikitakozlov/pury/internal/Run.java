package com.nikitakozlov.pury.internal;

public class Run {
    private Stage mRootStage;

    public void startRun(String stageName, int stageOrder) {
        mRootStage = new Stage(stageName, stageOrder);
        mRootStage.start();
    }

    public void startStage(String stageName, int stageOrder) {
        if (mRootStage.getOrder() < stageOrder) {
            mRootStage.startStage(stageName, stageOrder);
        }
    }

    public void stopStage(String stageName) {
        if (mRootStage != null) {
            mRootStage.stop(stageName);
        }
    }

    public boolean isStopped() {
        if (mRootStage != null) {
            return mRootStage.isStopped();
        }
        return false;
    }
}
