package com.nikitakozlov.pury.async;

import android.util.Log;

import com.nikitakozlov.pury.internal.MethodProfileResult;
import com.nikitakozlov.pury.internal.ProfilerId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncProfilingManager {
    private static AsyncProfilingManager sInstance;

    public static AsyncProfilingManager getInstance() {
        if (sInstance == null) {
            sInstance = new AsyncProfilingManager();
        }
        return sInstance;
    }

    private final Map<ProfilerId, AsyncProfiler> mAsyncProfilers;
    private final AsyncProfiler.Callback mAsyncProfilerCallback = new AsyncProfiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId, MethodProfileResult result) {
            mAsyncProfilers.remove(profilerId);
            if (result != null) {
                Log.d("AsyncProfiling", result.toString());
            }
        }
    };

    private AsyncProfilingManager() {
        mAsyncProfilers = new ConcurrentHashMap<>();
    }

    public AsyncProfiler getAsyncProfiler(ProfilerId profilerId) {
        if (mAsyncProfilers.containsKey(profilerId)) {
            return mAsyncProfilers.get(profilerId);
        }
        AsyncProfiler methodProfiler = new AsyncProfiler(profilerId, mAsyncProfilerCallback);
        mAsyncProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
