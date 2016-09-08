package com.nikitakozlov.pury_example;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ArticleListActivity extends AppCompatActivity {

    private static final int VISIBLE_THRESHOLD = 8;
    private final static int ARTICLE_PADDING = 4;
    private final static int SPAN_COUNT = 2;

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
                loadNextPage();
            }
        }
    };

    private ArticleAdapter mArticleAdapter;
    private int mPage = 0;
    private boolean mIsLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        initRecyclerView();
        loadNextPage();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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

    private void loadNextPage() {
        mIsLoading = true;
        mArticleAdapter.addItems(mArticleDataSource.getNextPage(mPage + 1));
        mPage++;
        mIsLoading = false;
    }

}
