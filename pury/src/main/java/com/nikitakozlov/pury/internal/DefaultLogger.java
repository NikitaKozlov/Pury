package com.nikitakozlov.pury.internal;

import android.util.Log;

import com.nikitakozlov.pury.Logger;

public class DefaultLogger implements Logger {
    @Override
    public void result(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void warning(String tag, String message) {
        Log.w(tag, message);
    }

    @Override
    public void error(String tag, String message) {
        Log.e(tag, message);
    }
}
