package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.Plugin;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

import java.util.HashMap;
import java.util.Map;

public class ResultManager {
    private final Map<String, Plugin> plugins = new HashMap<>();

    public void dispatchResult(ProfileResult result, ProfilerId profilerId) {
        for (String key : plugins.keySet()) {
            plugins.get(key).handleResult(result, profilerId);
        }
    }

    public void addPlugin(String key, Plugin plugin) {
        plugins.put(key, plugin);
    }

    public void removePlugin(String key) {
        plugins.remove(key);
    }
}
