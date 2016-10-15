package com.nikitakozlov.pury.profile;

public class Run {
    private final Stage mRootStage;

    public Run(String rootStageName, int rootStageOrder) {
        mRootStage = new Stage(rootStageName, rootStageOrder);
        mRootStage.start();
    }

    public StageError startStage(String stageName, int stageOrder) {
        return mRootStage.startStage(stageName, stageOrder);
    }

    public StageError stopStage(String stageName) {
         return mRootStage.stop(stageName);
    }

    public boolean isStopped() {
        return mRootStage.isStopped();
    }

    public Stage getRootStage() {
        return mRootStage;
    }
}
