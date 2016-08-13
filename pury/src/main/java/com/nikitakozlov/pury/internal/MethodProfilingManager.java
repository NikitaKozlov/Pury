package com.nikitakozlov.pury.internal;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodProfilingManager {
    private static MethodProfilingManager sInstance;

    public static MethodProfilingManager getInstance() {
        if (sInstance == null) {
            sInstance = new MethodProfilingManager();
        }
        return sInstance;
    }

    private final Map<ProfilerId, MethodProfiler> mMethodProfilers;
    private final MethodProfiler.Callback mMethodProfilerCallback = new MethodProfiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId, MethodProfileResult result) {
            mMethodProfilers.remove(profilerId);
            if (result != null) {
                Log.d("MethodProfiling", result.toString());
            }
        }
    };

    private MethodProfilingManager() {
        mMethodProfilers = new ConcurrentHashMap<>();
    }

    public MethodProfiler getMethodProfiler(ProfilerId profilerId) {
        if (mMethodProfilers.containsKey(profilerId)) {
            return mMethodProfilers.get(profilerId);
        }
        MethodProfiler methodProfiler = new MethodProfiler(profilerId, mMethodProfilerCallback);
        mMethodProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
