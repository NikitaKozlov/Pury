package com.nikitakozlov.pury.profile;

public class RunFactory {
    public Run startNewRun(String stageName, int stageOrder) {
        return new Run(stageName, stageOrder);
    }
}
