package com.nikitakozlov.pury.internal;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodProfiler {

    private final ProfilerId mProfilerId;
    private final int mRunsCounter;
    private final Map<Integer, StopWatch> mStopWatches;
    private final Callback mCallback;
    private volatile int mFinishedRuns;

    MethodProfiler(ProfilerId profilerId, @NonNull Callback callback) {
        mProfilerId = profilerId;
        mRunsCounter = profilerId.getRunsCounter();
        mStopWatches = new ConcurrentHashMap<>();
        mCallback = callback;
        mFinishedRuns = 0;
    }

    public Integer startRun() {
        if (mStopWatches.size() < mRunsCounter) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            mStopWatches.put(stopWatch.hashCode(), stopWatch);
            return stopWatch.hashCode();
        }
        throw new IllegalStateException("Too many starts for one profiler");
    }

    public void stopRun(Integer runId) {
        if (mStopWatches.containsKey(runId)) {
            mStopWatches.get(runId).stop();
            mFinishedRuns++;
            logIfFinished();
        } else {
            throw new IllegalStateException("No such runId in this profiler");
        }
    }

    private void logIfFinished() {
        if (mRunsCounter == mFinishedRuns) {
            Log.d(mProfilerId.getMethodId(), "done");
            mCallback.onDone(mProfilerId);
        }
    }

    interface Callback {
        void onDone(ProfilerId profilerId);
    }
}
