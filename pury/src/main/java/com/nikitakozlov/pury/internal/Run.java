package com.nikitakozlov.pury.internal;

public class Run {
    private final Stage mRootStage;

    public static Run startRun(String rootStageName, int rootStageOrder) {
        return new Run(rootStageName, rootStageOrder);
    }

    private Run(String rootStageName, int rootStageOrder) {
        mRootStage = new Stage(rootStageName, rootStageOrder);
        mRootStage.start();
    }

    public boolean startStage(String stageName, int stageOrder) {
        return mRootStage.startStage(stageName, stageOrder);
    }

    public void stopStage(String stageName) {
        mRootStage.stop(stageName);
    }

    public boolean isStopped() {
        return mRootStage.isStopped();
    }

    public Stage getRootStage() {
        return mRootStage;
    }
}
