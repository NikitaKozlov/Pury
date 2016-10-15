package com.nikitakozlov.pury.result;

import com.nikitakozlov.pury.ResultHandler;
import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

import java.util.HashMap;
import java.util.Map;

public class ResultManager {
    private final Map<String, ResultHandler> resultHandlers = new HashMap<>();

    public void dispatchResult(ProfileResult result, ProfilerId profilerId) {
        for (String key : resultHandlers.keySet()) {
            resultHandlers.get(key).handleResult(result, profilerId);
        }
    }

    public void addResultHandler(String key, ResultHandler resultHandler) {
        resultHandlers.put(key, resultHandler);
    }

    public void removeResultHandler(String key) {
        resultHandlers.remove(key);
    }
}
