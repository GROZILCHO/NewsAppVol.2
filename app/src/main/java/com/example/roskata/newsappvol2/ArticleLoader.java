package com.example.roskata.newsappvol2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchArticleData(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
