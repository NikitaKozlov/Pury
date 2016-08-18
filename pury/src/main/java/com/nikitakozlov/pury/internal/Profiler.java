package com.nikitakozlov.pury.internal;

import android.support.annotation.NonNull;

import com.nikitakozlov.pury.method.MethodProfileResult;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Profiler {
    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final List<Run> mRuns;
    private Run mActiveRun;
    private final Callback mCallback;
    private volatile int mFinishedRuns;

    public Profiler(ProfilerId profilerId, @NonNull Callback callback) {
        mProfilerId = profilerId;
        mRunsCounter = profilerId.getRunsCounter();
        mRuns = new CopyOnWriteArrayList<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    public void startStage(String stageName, int stageOrder) {
        if (mActiveRun == null || mActiveRun.isStopped()) if (mRuns.size() < mRunsCounter) {
            mActiveRun = new Run();
            mActiveRun.startStage(stageName, stageOrder);
            mRuns.add(mActiveRun);
        } else if (mActiveRun != null && !mActiveRun.isStopped()) {
            mActiveRun.startStage(stageName, stageOrder);
        }
    }

    public void stopStage() {
        if (mActiveRun != null && !mActiveRun.isStopped()) {

            logIfFinished();
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            //mCallback.onDone(mProfilerId, MethodProfileProcessor.process(mProfilerId, mStopWatches));
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId, MethodProfileResult result);
    }
}
