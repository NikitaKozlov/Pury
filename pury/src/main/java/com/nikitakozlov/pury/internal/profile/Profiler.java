package com.nikitakozlov.pury.internal.profile;

import android.support.annotation.NonNull;

import com.nikitakozlov.pury.internal.Logger;
import com.nikitakozlov.pury.internal.result.ProfileResult;
import com.nikitakozlov.pury.internal.result.ProfileResultProcessor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Profiler {

    private static final int START_ORDER = 0;
    private static final String LOG_TAG = "Pury";

    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final Callback mCallback;
    private final ProfileResultProcessor mResultProcessor;
    private final Logger mLogger;
    private final RunFactory mRunFactory;
    private final List<Run> mRuns;
    private Run mActiveRun;
    private volatile int mFinishedRuns;

    public Profiler(ProfilerId profilerId, @NonNull Callback callback,
                    ProfileResultProcessor resultProcessor, Logger logger, RunFactory runFactory) {
        mProfilerId = profilerId;
        mRunFactory = runFactory;
        mRunsCounter = profilerId.getRunsCounter();
        this.mResultProcessor = resultProcessor;
        this.mLogger = logger;
        mRuns = new CopyOnWriteArrayList<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    public void startStage(String stageName, int stageOrder) {
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
            mLogger.warning(LOG_TAG, "MethodId = \"" + mProfilerId.getMethodId() + "\". " +
                    "Can't start Run, stage order for stage \"" + stageName + "\" is wrong. " +
                    "Expected value: " + START_ORDER + ", actual value: " + stageOrder + ".");
            return;
        }
        mActiveRun = mRunFactory.startNewRun(stageName, stageOrder);
        mRuns.add(mActiveRun);
    }

    public void stopStage(String stageName) {
        if (mActiveRun != null && !mActiveRun.isStopped()) {
            logIfStageError(mActiveRun.stopStage(stageName));

            if (mActiveRun.isStopped()) {
                mFinishedRuns++;
                logIfFinished();
            }
        }
    }

    private void logIfStageError(StageError stageError) {
        if (stageError != null && !stageError.isInternal()) {
            mLogger.error(LOG_TAG, "MethodId = \"" + mProfilerId.getMethodId() + "\". " + StageErrorUtils.format(stageError));
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            ProfileResult profileResult = mResultProcessor.process(mRuns);
            mLogger.result(LOG_TAG, "Profiling results for " + mProfilerId.getMethodId() + ":\n" + profileResult.toString());
            mCallback.onDone(mProfilerId);
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId);
    }
}
