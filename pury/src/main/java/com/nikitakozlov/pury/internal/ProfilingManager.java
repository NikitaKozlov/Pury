package com.nikitakozlov.pury.internal;

import android.util.Log;

import com.nikitakozlov.pury.method.MethodProfileResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfilingManager {
    private static ProfilingManager sInstance;

    public static ProfilingManager getInstance() {
        if (sInstance == null) {
            sInstance = new ProfilingManager();
        }
        return sInstance;
    }

    //For Testing only
    public static void setInstance(ProfilingManager sInstance) {
        ProfilingManager.sInstance = sInstance;
    }

    private final Map<ProfilerId, Profiler> mAsyncProfilers;
    private final Profiler.Callback mAsyncProfilerCallback = new Profiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId, MethodProfileResult result) {
            mAsyncProfilers.remove(profilerId);
            if (result != null) {
                Log.d("AsyncProfiling", result.toString());
            }
        }
    };

    private ProfilingManager() {
        mAsyncProfilers = new ConcurrentHashMap<>();
    }

    public Profiler getProfiler(ProfilerId profilerId) {
        if (mAsyncProfilers.containsKey(profilerId)) {
            return mAsyncProfilers.get(profilerId);
        }
        Profiler methodProfiler = new Profiler(profilerId, mAsyncProfilerCallback);
        mAsyncProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
