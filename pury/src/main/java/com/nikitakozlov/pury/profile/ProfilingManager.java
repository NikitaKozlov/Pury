package com.nikitakozlov.pury.profile;

import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.result.ResultManager;

import java.util.HashMap;
import java.util.Map;

public class ProfilingManager {

    private final ResultManager mResultManager;
    private final Map<ProfilerId, Profiler> mProfilers = new HashMap<>();
    private final ProfileResultProcessor mResultProcessor = new ProfileResultProcessor();
    private final RunFactory mRunFactory = new RunFactory();

    private final Profiler.Callback mProfilerCallback = new Profiler.Callback() {
        @Override
        public void onDone(ProfilerId profilerId) {
            removeProfiler(profilerId);
        }
    };

    public ProfilingManager(ResultManager resultManager) {
        mResultManager = resultManager;
    }

    public synchronized Profiler getProfiler(ProfilerId profilerId) {
        if (mProfilers.containsKey(profilerId)) {
            return mProfilers.get(profilerId);
        }
        Profiler methodProfiler = new Profiler(profilerId, mProfilerCallback, mResultProcessor,
                mResultManager, Pury.getLogger(), mRunFactory);
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
