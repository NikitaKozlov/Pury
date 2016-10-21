package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.Plugin;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

import java.util.HashMap;
import java.util.Map;

public class ResultManager {
    private final Map<String, Plugin> resultHandlers = new HashMap<>();

    public void dispatchResult(ProfileResult result, ProfilerId profilerId) {
        for (String key : resultHandlers.keySet()) {
            resultHandlers.get(key).handleResult(result, profilerId);
        }
    }

    public void addPlugin(String key, Plugin plugin) {
        resultHandlers.put(key, plugin);
    }

    public void removePlugin(String key) {
        resultHandlers.remove(key);
    }
}
