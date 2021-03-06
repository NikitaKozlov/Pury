package com.nikitakozlov.pury.profile;


import com.nikitakozlov.pury.Logger;
import com.nikitakozlov.pury.result.ResultManager;
import com.nikitakozlov.pury.result.model.ProfileResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.nikitakozlov.pury.Pury.LOG_TAG;

public class Profiler {

    static final int START_ORDER = 0;

    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final Callback mCallback;
    private final ProfileResultProcessor mResultProcessor;
    private final ResultManager mResultManager;
    private final Logger mLogger;
    private final RunFactory mRunFactory;
    private final List<Run> mRuns;
    private Run mActiveRun;
    private volatile int mFinishedRuns;

    Profiler(ProfilerId profilerId, Callback callback,
             ProfileResultProcessor resultProcessor, ResultManager resultManager, Logger logger, RunFactory runFactory) {
        mProfilerId = profilerId;
        mRunFactory = runFactory;
        mRunsCounter = profilerId.getRunsCounter();
        mResultProcessor = resultProcessor;
        mResultManager = resultManager;
        mLogger = logger;
        mRuns = new CopyOnWriteArrayList<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    /**
     * @param stageName  Name of a stage to start. Used in results.
     * @param stageOrder Stage order must be bigger then order of current most nested active stage.
     *                   First profiling must starts with value 0.
     */
    public synchronized void startStage(String stageName, int stageOrder) {
        if (mActiveRun == null || mActiveRun.isStopped()) {
            if (mRuns.size() < mRunsCounter) {
                startRun(stageName, stageOrder);
            }
        } else if (mActiveRun != null && !mActiveRun.isStopped()) {
            logIfStageError(mActiveRun.startStage(stageName, stageOrder));
        }
    }

    private void startRun(String stageName, int stageOrder) {
        if (stageOrder != START_ORDER) {
            mLogger.warning(LOG_TAG, "Profiler Name = \"" + mProfilerId.getProfilerName() + "\". " +
                    "Can't start Run, stage order for stage \"" + stageName + "\" is wrong. " +
                    "Expected value: " + START_ORDER + ", actual value: " + stageOrder + ".");
            return;
        }
        mActiveRun = mRunFactory.startNewRun(stageName, stageOrder);
        mRuns.add(mActiveRun);
    }

    /**
     * @param stageName Name of stage to stop. Used in results.
     */
    public synchronized void stopStage(String stageName) {
        if (mActiveRun != null) {

            StageError stageError = mActiveRun.stopStage(stageName);

            if (stageError == null) {
                if (mActiveRun.isStopped()) {
                    mFinishedRuns++;
                    logIfFinished();
                }
            } else {
                logIfStageError(stageError);
            }
        }
    }

    private void logIfStageError(StageError stageError) {
        if (stageError != null && !stageError.isInternal()) {
            mLogger.error(LOG_TAG, "Profiler Name = \"" + mProfilerId.getProfilerName() + "\". " + StageErrorUtils.format(stageError));
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            ProfileResult profileResult = mResultProcessor.process(mRuns);
            mResultManager.dispatchResult(profileResult, mProfilerId);
            mCallback.onDone(mProfilerId);
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId);
    }
}
