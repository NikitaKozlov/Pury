package com.nikitakozlov.pury.internal.profile;

import android.util.Log;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.internal.result.ProfileResultProcessor;

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
    private final ProfileResultProcessor mResultProcessor;

    private final Profiler.Callback mAsyncProfilerCallback = new Profiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId) {
            mAsyncProfilers.remove(profilerId);
        }
    };

    private ProfilingManager() {
        mAsyncProfilers = new ConcurrentHashMap<>();
        mResultProcessor = new ProfileResultProcessor();
    }

    public Profiler getProfiler(ProfilerId profilerId) {
        if (mAsyncProfilers.containsKey(profilerId)) {
            return mAsyncProfilers.get(profilerId);
        }
        Profiler methodProfiler = new Profiler(profilerId, mAsyncProfilerCallback, mResultProcessor,
                Pury.getLogger());
        mAsyncProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
