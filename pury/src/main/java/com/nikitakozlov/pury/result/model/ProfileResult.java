package com.nikitakozlov.pury.result.model;

import java.util.List;

public interface ProfileResult {
    List<? extends ProfileResult> getNestedResults();
}
