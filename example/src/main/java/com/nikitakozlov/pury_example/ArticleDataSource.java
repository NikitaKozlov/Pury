package com.nikitakozlov.pury_example;

import java.util.ArrayList;
import java.util.List;

public class ArticleDataSource {
    private static final String ARTICLE = "Article ";
    private static final int ARTICLES_PER_PAGE = 16;

    public List<String> getNextPage(int pageNumber) {
        List<String> articles = new ArrayList<>();

        for (int i = (pageNumber - 1) * ARTICLES_PER_PAGE; i < pageNumber * ARTICLES_PER_PAGE; i++) {
            articles.add(ARTICLE + i);
        }
        return articles;
    }
}
