package com.nikitakozlov.pury;

public interface Logger {
    void result(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);
}
