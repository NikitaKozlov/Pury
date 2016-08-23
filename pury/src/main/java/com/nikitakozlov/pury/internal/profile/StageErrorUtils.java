package com.nikitakozlov.pury.internal.profile;

public final class StageErrorUtils {
    private StageErrorUtils() {}

    public static String format(StageError error) {
        switch (error.getType()) {
            case START_TO_SMALL_ORDER:
                return "You are trying to start stage: \"" + error.getIntendedStageName() +
                        "\", but it's order value is smaller or equal to parent's stage has: " +
                        error.getIntendedStageOrder() + ". Parent stage: name = \"" +
                        error.getParentStageName() + "\", order value = " + error.getParentStageOrder() + ".";
            case START_PARENT_STAGE_IS_STOPPED:
                break;
            case STOP_NOT_STARTED_STAGE:
                break;
            case STOPPED_ALREADY:
                break;
            default:
                break;
        }
        return error.toString();
    }
}
