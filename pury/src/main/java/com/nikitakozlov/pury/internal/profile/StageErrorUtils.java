package com.nikitakozlov.pury.internal.profile;

public final class StageErrorUtils {
    private StageErrorUtils() {
    }

    public static String format(StageError error) {
        switch (error.getType()) {
            case START_TO_SMALL_ORDER:
                return "You are trying to start stage: \"" + error.getIntendedStageName() +
                        "\", but it's order value is smaller or equal to parent's stage has: " +
                        error.getIntendedStageOrder() + ". Parent stage: name = \"" +
                        error.getParentStageName() + "\", order value = " + error.getParentStageOrder() + ".";
            case STOP_NOT_STARTED_STAGE:
                return "You are trying to stop stage: \"" + error.getIntendedStageName() +
                        "\" with order value " + error.getIntendedStageOrder() +
                        ", but it wasn't started.";
            case STOPPED_ALREADY:
                return "You are trying to stop stage: \"" + error.getIntendedStageName() +
                        "\" with order value " + error.getIntendedStageOrder() +
                        ", but it is already stopped.";
            default:
                break;
        }
        return error.toString();
    }
}
