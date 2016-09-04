package com.nikitakozlov.pury;

import com.nikitakozlov.pury.internal.DefaultLogger;
import com.nikitakozlov.pury.internal.Logger;
import com.nikitakozlov.pury.internal.profile.ProfilerId;
import com.nikitakozlov.pury.internal.profile.ProfilingManager;
import com.nikitakozlov.pury.internal.profile.StageId;

public final class Pury {
    static volatile Logger sLogger;
    static volatile boolean sEnabled;

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public synchronized static Logger getLogger() {
        if (sLogger == null) {
            sLogger = new DefaultLogger();
        }
        return sLogger;
    }

    public static boolean isEnabled() {
        return sEnabled;
    }

    public synchronized static void setEnabled(boolean enabled) {
        if (!enabled) {
            ProfilingManager.getInstance().clear();
        }
        sEnabled = enabled;
    }

    public static void startProfiling(String profilerName, String stageName, int stageOrder,
                                      int runsCounter) {
        ProfilerId profilerId = new ProfilerId(profilerName, runsCounter);
        ProfilingManager.getInstance().getProfiler(profilerId).startStage(stageName, stageOrder);
    }

    public static void stopProfiling(String profilerName, String stageName, int runsCounter) {
        ProfilerId profilerId = new ProfilerId(profilerName, runsCounter);
        ProfilingManager.getInstance().getProfiler(profilerId).stopStage(stageName);
    }
}
