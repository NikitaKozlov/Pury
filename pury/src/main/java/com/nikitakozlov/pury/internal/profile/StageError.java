package com.nikitakozlov.pury.internal.profile;

public class StageError {

    public static StageError createError(String intendedStageName, Stage parentStage, Type type) {
        return createError(intendedStageName, 0, parentStage, type);
    }

    public static StageError createError(String intendedStageName, int intendedStageOrder,
        Stage parentStage, Type type) {
        return new StageError(intendedStageName, intendedStageOrder, parentStage.getName(),
                parentStage.getOrder(), type);
    }

    private final String intendedStageName;
    private final int intendedStageOrder;
    private final String parentStageName;
    private final int parentStageOrder;
    private final Type type;

    private StageError(String intendedStageName, int intendedStageOrder, String parentStageName,
                      int parentStageOrder, Type type) {
        this.intendedStageName = intendedStageName;
        this.intendedStageOrder = intendedStageOrder;
        this.parentStageName = parentStageName;
        this.parentStageOrder = parentStageOrder;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getIntendedStageName() {
        return intendedStageName;
    }

    public int getIntendedStageOrder() {
        return intendedStageOrder;
    }

    public String getParentStageName() {
        return parentStageName;
    }

    public int getParentStageOrder() {
        return parentStageOrder;
    }

    public boolean isInternal() {
        return type.isInternal();
    }

    public enum Type {

        START_RUN_TO_SMALL_ORDER,
        START_TO_SMALL_ORDER,
        START_PARENT_STAGE_IS_STOPPED(true),
        START_PARENT_STAGE_NOT_STARTED(true),
        STOP_NOT_STARTED_STAGE,
        STOPPED_ALREADY;

        private final boolean internal;

        Type() {
            internal = false;
        }

        Type(boolean internal) {
            this.internal = internal;
        }

        public boolean isInternal() {
            return internal;
        }
    }
}
