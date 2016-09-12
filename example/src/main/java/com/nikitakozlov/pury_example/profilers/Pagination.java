package com.nikitakozlov.pury_example.profilers;

public class Pagination {
    public static final String PROFILER_NAME = "Pagination";
    public static final int RUN_COUNTER = 5;

    public static final String TOP_STAGE ="Get Next Page";
    public static final int TOP_STAGE_ORDER = 0;

    public static final String LOAD_PAGE = "Load";
    public static final int LOAD_PAGE_ORDER = TOP_STAGE_ORDER + 1;

    public static final String PROCESS_PAGE = "Process";
    public static final int PROCESS_PAGE_ORDER = LOAD_PAGE_ORDER + 1;
}
