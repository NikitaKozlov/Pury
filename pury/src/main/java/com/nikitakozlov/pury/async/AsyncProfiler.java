package com.nikitakozlov.pury.async;

import android.support.annotation.NonNull;

import com.nikitakozlov.pury.internal.MethodProfileProcessor;
import com.nikitakozlov.pury.internal.MethodProfileResult;
import com.nikitakozlov.pury.internal.ProfilerId;
import com.nikitakozlov.pury.internal.StopWatch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AsyncProfiler {

    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final List<StopWatch> mStopWatches;
    private final Callback mCallback;
    private volatile int mFinishedRuns;

    AsyncProfiler(ProfilerId profilerId, @NonNull Callback callback) {
        mProfilerId = profilerId;
        mRunsCounter = profilerId.getRunsCounter();
        mStopWatches = new CopyOnWriteArrayList<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    public Integer startRun() {
        if (mStopWatches.size() < mRunsCounter) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            mStopWatches.add(stopWatch);
            return stopWatch.hashCode();
        }
        throw new IllegalStateException("Too many starts for one profiler");
    }

    public void stopRun() {
        if (mFinishedRuns < mStopWatches.size()) {
            mStopWatches.get(mFinishedRuns).stop();
            mFinishedRuns++;
            logIfFinished();
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            mCallback.onDone(mProfilerId, MethodProfileProcessor.process(mProfilerId, mStopWatches));
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId, MethodProfileResult result);
    }
}
