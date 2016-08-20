package com.nikitakozlov.pury.internal;

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
