package com.nikitakozlov.pury.internal.profile;

import android.support.annotation.NonNull;

import com.nikitakozlov.pury.internal.Logger;
import com.nikitakozlov.pury.internal.result.ProfileResultProcessor;
import com.nikitakozlov.pury.method.MethodProfileResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Profiler {
    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final Callback mCallback;
    private final ProfileResultProcessor resultProcessor;
    private final Logger logger;
    private final List<Run> mRuns;
    private Run mActiveRun;
    private volatile int mFinishedRuns;

    public Profiler(ProfilerId profilerId, @NonNull Callback callback,
                    ProfileResultProcessor resultProcessor, Logger logger) {
        mProfilerId = profilerId;
        mRunsCounter = profilerId.getRunsCounter();
        this.resultProcessor = resultProcessor;
        this.logger = logger;
        mRuns = new CopyOnWriteArrayList<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    public void startStage(String stageName, int stageOrder) {
        if (mActiveRun == null || mActiveRun.isStopped()) if (mRuns.size() < mRunsCounter) {
            mActiveRun = Run.startRun(stageName, stageOrder);
            mRuns.add(mActiveRun);
        } else if (mActiveRun != null && !mActiveRun.isStopped()) {
            mActiveRun.startStage(stageName, stageOrder);
        }
    }

    public void stopStage(String stageName) {
        if (mActiveRun != null && !mActiveRun.isStopped()) {
            mActiveRun.stopStage(stageName);
            if (mActiveRun.isStopped()) {
                mFinishedRuns++;
                logIfFinished();
            }
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            mCallback.onDone(mProfilerId, null);
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId, MethodProfileResult result);
    }
}
