package com.nikitakozlov.pury.profile;

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


    public StageError startStage(String stageName, int stageOrder) {
        if (mOrder >= stageOrder) {
            return StageError.createError(stageName, stageOrder, this, StageError.Type.START_TO_SMALL_ORDER);
        }

        if (mIsStopped) {
            return StageError.createError(stageName, stageOrder, this, StageError.Type.START_PARENT_STAGE_IS_STOPPED);
        }

        if (!mIsStarted) {
            return StageError.createError(stageName, stageOrder, this, StageError.Type.START_PARENT_STAGE_NOT_STARTED);
        }

        if (mActiveNestedStage == null || mActiveNestedStage.mIsStopped) {
            startNewNestedStage(stageName, stageOrder);
            return null;
        } else {
            return mActiveNestedStage.startStage(stageName, stageOrder);
        }
    }

    private void startNewNestedStage(String stageName, int stageOrder) {
        Stage stage = new Stage(stageName, stageOrder);
        stage.start();
        mActiveNestedStage = stage;
        mStages.add(stage);
    }

    public StageError stop(String stageName) {
        if (mName.equals(stageName)) {
            if (!mIsStopped) {
                mStopWatch.stop();
                mIsStopped = true;
                mIsStarted = false;
                if (mActiveNestedStage != null) {
                    mActiveNestedStage.stop(mActiveNestedStage.getName());
                }
            } else {
                return StageError.createError(stageName, this, StageError.Type.STOPPED_ALREADY);
            }
        } else if (mActiveNestedStage != null) {
            return mActiveNestedStage.stop(stageName);
        } else {
            return StageError.createError(stageName, this, StageError.Type.STOP_NOT_STARTED_STAGE);
        }

        return null;
    }

    public boolean isStopped() {
        return mIsStopped;
    }

    boolean isStarted() {
        return mIsStarted;
    }

    public long getStartTime() {
        return mStopWatch.getStartTime();
    }
    public long getExecTime() {
        return mStopWatch.getExecTime();
    }

    public List<Stage> getStages() {
        return mStages;
    }
}
