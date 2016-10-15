package com.nikitakozlov.pury;

import com.nikitakozlov.pury.profile.ProfilerId;
import com.nikitakozlov.pury.result.model.ProfileResult;

public interface ResultHandler {

    void handleResult(ProfileResult result, ProfilerId profilerId);

}
