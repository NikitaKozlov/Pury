package com.nikitakozlov.pury.internal;

public class ProfilerId {
    private final String methodId;
    private final int runsCounter;

    public ProfilerId(String methodId, int runsCounter) {
        this.methodId = methodId;
        this.runsCounter = runsCounter;
    }

    public String getMethodId() {
        return methodId;
    }

    public int getRunsCounter() {
        return runsCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilerId that = (ProfilerId) o;

        if (runsCounter != that.runsCounter) return false;
        return methodId != null ? methodId.equals(that.methodId) : that.methodId == null;

    }

    @Override
    public int hashCode() {
        int result = methodId != null ? methodId.hashCode() : 0;
        result = 31 * result + runsCounter;
        return result;
    }
}
