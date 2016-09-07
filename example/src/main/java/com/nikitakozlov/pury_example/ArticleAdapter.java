package com.nikitakozlov.pury_example;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private final List<String> articles = new ArrayList<>();
    private final Random random = new Random();

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256)));
        holder.text.setText(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void addItems(List<String> newArticles) {
        articles.addAll(newArticles);
        notifyItemRangeInserted(articles.size(), newArticles.size());
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
