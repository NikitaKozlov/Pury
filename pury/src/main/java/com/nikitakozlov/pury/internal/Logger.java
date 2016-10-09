package com.nikitakozlov.pury.internal;

public interface Logger {
    void result(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);
}
