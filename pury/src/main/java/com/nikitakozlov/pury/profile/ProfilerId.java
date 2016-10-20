package com.nikitakozlov.pury.profile;

/**
 * Used to identify Profiler
 */
public class ProfilerId {
    private final String mProfilerName;
    private final int mRunsCounter;

    /**
     * @param profilerName Profiler Name.
     * @param runsCounter Amount of runs to complete
     */
    public ProfilerId(String profilerName, int runsCounter) {
        this.mProfilerName = profilerName;
        this.mRunsCounter = runsCounter;
    }

    /**
     * @return Profiler Name
     */
    public String getProfilerName() {
        return mProfilerName;
    }

    /**
     * @return RunCounter that represents amount of runs to complete
     */
    public int getRunsCounter() {
        return mRunsCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilerId that = (ProfilerId) o;

        if (mRunsCounter != that.mRunsCounter) return false;
        return mProfilerName != null ? mProfilerName.equals(that.mProfilerName) : that.mProfilerName == null;

    }

    @Override
    public int hashCode() {
        int result = mProfilerName != null ? mProfilerName.hashCode() : 0;
        result = 31 * result + mRunsCounter;
        return result;
    }
}
