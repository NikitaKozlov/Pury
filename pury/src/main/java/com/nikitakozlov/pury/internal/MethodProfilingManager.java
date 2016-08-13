package com.nikitakozlov.pury.internal;

import java.util.HashMap;
import java.util.Map;

public class MethodProfilingManager {
    private static MethodProfilingManager sInstance;

    public static MethodProfilingManager getInstance() {
        if (sInstance == null) {
            sInstance = new MethodProfilingManager();
        }
        return sInstance;
    }

    private final Map<ProfilerId, MethodProfiler> mMethodProfilers;

    private MethodProfilingManager() {
        mMethodProfilers = new HashMap<>();
    }

    public MethodProfiler getMethodProfiler(ProfilerId profilerId) {
        if (mMethodProfilers.containsKey(profilerId)) {
            return mMethodProfilers.get(profilerId);
        }
        MethodProfiler methodProfiler = new MethodProfiler(profilerId,
                new MethodProfiler.Callback() {
                    @Override
                    public void onDone(ProfilerId profilerId) {
                        mMethodProfilers.remove(profilerId);
                    }
                });
        mMethodProfilers.put(profilerId, methodProfiler);
        return methodProfiler;
    }

}
