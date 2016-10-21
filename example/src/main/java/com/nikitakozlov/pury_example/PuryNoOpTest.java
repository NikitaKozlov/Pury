package com.nikitakozlov.pury_example;

import com.nikitakozlov.pury.Logger;
import com.nikitakozlov.pury.Plugin;
import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

public class PuryNoOpTest {

    public static void test() {
        String testNoopProfilerName = "Test noop";

        Pury.getLogger();
        Pury.isEnabled();
        Pury.setEnabled(true);
        Pury.startProfiling(testNoopProfilerName, "", 0, 1);
        Pury.stopProfiling(testNoopProfilerName, "", 1);
        Pury.setLogger(new Logger() {
            @Override
            public void result(String s, String s1) {

            }

            @Override
            public void warning(String s, String s1) {

            }

            @Override
            public void error(String s, String s1) {

            }
        });
        Pury.setLogger(null);

        testPlugins();
    }

    private static void testPlugins() {
        String key = "key";
        Pury.addPlugin(key, new Plugin() {
            @Override
            public void handleResult(ProfileResult result, ProfilerId profilerId) {

            }
        });
        Pury.removePlugin(key);
    }
}
