package com.nikitakozlov.pury_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private static final List<String> ARTICLES = Arrays.asList("Article 1", "Article 2",
            "Article 3", "Article 4", "Article 5", "Article 6", "Article 7", "Article 8",
            "Article 9", "Article 10");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle args = new Bundle();
                args.putString(ArticleFragment.ARTICLE, ARTICLES.get(position));
                Fragment fragment = new ArticleFragment();
                fragment.setArguments(args);
                return fragment;
            }

            @Override
            public int getCount() {
                return ARTICLES.size();
            }
        });
    }
}
