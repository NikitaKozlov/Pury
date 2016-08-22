package com.nikitakozlov.pury.internal.profile;

public class StageError {

    private final Type type;

    public StageError(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {

        START_TO_SMALL_ORDER,
        START_PARENT_STAGE_IS_STOPPED,
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
