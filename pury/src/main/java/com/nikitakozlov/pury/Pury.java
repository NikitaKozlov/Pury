package com.nikitakozlov.pury;

import com.nikitakozlov.pury.internal.DefaultLogger;
import com.nikitakozlov.pury.internal.Logger;
import com.nikitakozlov.pury.internal.profile.ProfilingManager;

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
}
