package com.nikitakozlov.pury_example.profilers;

public class Pagination {
    public static final String PROFILER_NAME = "Pagination";
    public static final int RUN_COUNTER = 5;

    public static final String TOP_STAGE ="Get Next Page";
    public static final int TOP_STAGE_ORDER = 0;

    public static final String LOAD_NEXT_PAGE = "Load Next Page";
    public static final int LOAD_NEXT_PAGE_ORDER = TOP_STAGE_ORDER + 1;

    public static final String PROCESS_NEXT_PAGE = "Process Next Page";
    public static final int PROCESS_NEXT_PAGE_ORDER = LOAD_NEXT_PAGE_ORDER + 1;
}
