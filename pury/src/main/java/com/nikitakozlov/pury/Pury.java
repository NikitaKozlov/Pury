package com.nikitakozlov.pury;

import com.nikitakozlov.pury.internal.DefaultLogger;
import com.nikitakozlov.pury.internal.Logger;

public final class Pury {
    static Logger sLogger;
    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public static Logger getLogger() {
        if (sLogger == null) {
            sLogger = new DefaultLogger();
        }
        return sLogger;
    }
}
