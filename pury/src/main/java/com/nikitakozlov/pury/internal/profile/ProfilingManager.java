package com.nikitakozlov.pury.internal.profile;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.internal.result.ProfileResultProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfilingManager {
    private static ProfilingManager sInstance = new ProfilingManager();

    public static ProfilingManager getInstance() {
        return sInstance;
    }

    //For Testing only
    static void setInstance(ProfilingManager instance) {
        sInstance = instance;
    }

    private final Map<ProfilerId, Profiler> mAsyncProfilers  = new ConcurrentHashMap<>();;
    private final ProfileResultProcessor mResultProcessor = new ProfileResultProcessor();
    private final RunFactory mRunFactory = new RunFactory();

    private final Profiler.Callback mAsyncProfilerCallback = new Profiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId) {
            mAsyncProfilers.remove(profilerId);
        }
    };

    private ProfilingManager() {
    }

    public synchronized Profiler getProfiler(ProfilerId profilerId) {
        if (mAsyncProfilers.containsKey(profilerId)) {
            return mAsyncProfilers.get(profilerId);
        }
        Profiler methodProfiler = new Profiler(profilerId, mAsyncProfilerCallback, mResultProcessor,
                Pury.getLogger(), mRunFactory);
        mAsyncProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
