package com.nikitakozlov.pury_example;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nikitakozlov.pury.annotations.MethodProfiling;
import com.nikitakozlov.pury.annotations.StartProfiling;
import com.nikitakozlov.pury.annotations.StopProfiling;
import com.nikitakozlov.pury_example.profilers.Pagination;

import java.util.List;
import java.util.Random;

public class ArticleListActivity extends AppCompatActivity {

    private static final int LOAD_PAGE_DELAY = 200;
    private static final int PAGE_PROCESS_DELAY = 50;
    private static final int DELAY_MAX_VARIATON = 50;

    private static final int VISIBLE_THRESHOLD = 8;
    private final static int ARTICLE_PADDING = 4;
    private final static int SPAN_COUNT = 2;

    private final Random random = new Random();
    private final ArticleDataSource mArticleDataSource = new ArticleDataSource();

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            if (mIsLoading) {
                return;
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - VISIBLE_THRESHOLD) {
                getNextPage();
            }
        }
    };

    private RecyclerView recyclerView;
    private ArticleAdapter mArticleAdapter;
    private volatile int mPage = 0;
    private boolean mIsLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        initRecyclerView();
        getNextPage();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = ARTICLE_PADDING;
                outRect.top = ARTICLE_PADDING;
                outRect.left = ARTICLE_PADDING;
                outRect.right = ARTICLE_PADDING;
            }
        });
        mArticleAdapter = new ArticleAdapter();
        recyclerView.setAdapter(mArticleAdapter);
        recyclerView.setOnScrollListener(scrollListener);
    }

    @StartProfiling(profilerName = Pagination.PROFILER_NAME, runsCounter = Pagination.RUN_COUNTER,
            stageName = Pagination.TOP_STAGE, stageOrder = Pagination.TOP_STAGE_ORDER)
    private void getNextPage() {

        if (mIsLoading) {
            return;
        }

        mIsLoading = true;
        new AsyncTask<Integer, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Integer... integers) {
                return processNextPage(loadNextPage(integers[0]));
            }

            @StopProfiling(profilerName = Pagination.PROFILER_NAME, runsCounter = Pagination.RUN_COUNTER,
                    stageName = Pagination.TOP_STAGE)
            @Override
            protected void onPostExecute(List<String> articles) {
                mArticleAdapter.addItems(articles);
                mPage++;
                mIsLoading = false;
            }
        }.execute(mPage + 1);
    }

    @MethodProfiling(profilerName = Pagination.PROFILER_NAME, runsCounter = Pagination.RUN_COUNTER,
            stageName = Pagination.LOAD_PAGE, stageOrder = Pagination.LOAD_PAGE_ORDER)
    private List<String> loadNextPage(Integer page) {
        try {
            Thread.sleep((long) (LOAD_PAGE_DELAY + random.nextFloat() * DELAY_MAX_VARIATON));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mArticleDataSource.getNextPage(page);
    }

    @MethodProfiling(profilerName = Pagination.PROFILER_NAME, runsCounter = Pagination.RUN_COUNTER,
            stageName = Pagination.PROCESS_PAGE, stageOrder = Pagination.PROCESS_PAGE_ORDER)
    private List<String> processNextPage(final List<String> articles) {
        try {
            Thread.sleep((long) (PAGE_PROCESS_DELAY + random.nextFloat() * DELAY_MAX_VARIATON));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
