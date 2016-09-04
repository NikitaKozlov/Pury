package com.nikitakozlov.pury.internal.profile;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.internal.result.ProfileResultProcessor;

import java.util.HashMap;
import java.util.Map;

public class ProfilingManager {
    private volatile static ProfilingManager sInstance = new ProfilingManager();

    public static ProfilingManager getInstance() {
        return sInstance;
    }

    //For Testing only
    static void setInstance(ProfilingManager instance) {
        sInstance = instance;
    }

    private final Map<ProfilerId, Profiler> mProfilers = new HashMap<>();
    private final ProfileResultProcessor mResultProcessor = new ProfileResultProcessor();
    private final RunFactory mRunFactory = new RunFactory();

    private final Profiler.Callback mProfilerCallback = new Profiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId) {
            removeProfiler(profilerId);
        }
    };

    private ProfilingManager() {
    }

    public synchronized Profiler getProfiler(ProfilerId profilerId) {
        if (mProfilers.containsKey(profilerId)) {
            return mProfilers.get(profilerId);
        }
        Profiler methodProfiler = new Profiler(profilerId, mProfilerCallback, mResultProcessor,
                Pury.getLogger(), mRunFactory);
        mProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

    private synchronized void removeProfiler(ProfilerId profilerId) {
        mProfilers.remove(profilerId);
    }

    public synchronized void clear() {
        mProfilers.clear();
    }
}
