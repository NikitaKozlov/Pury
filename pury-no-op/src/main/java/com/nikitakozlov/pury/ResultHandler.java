package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

/**
 * ResultHandler allows to customize result processing. For example instead of logging result send
 * it somewhere, or save it in a permanent storage.
 */
public interface ResultHandler {

    /**
     * Hook method that will be called once result is ready.
     * @param result Result of profiling
     * @param profilerId profiler identifier.
     */
    void handleResult(ProfileResult result, ProfilerId profilerId);

}
