package com.nikitakozlov.pury.profile;

public class ProfilingManager {

    /**
     *
     * @param profilerId ID that identifies a given Profiler.
     * @return {@link Profiler} that correspond to the given ProfilerId. If there is no
     *         corresponding Profiler yet, new instance will be created.
     */
    public synchronized Profiler getProfiler(ProfilerId profilerId) {
        return null;
    }

    public synchronized void clear() {
    }
}
