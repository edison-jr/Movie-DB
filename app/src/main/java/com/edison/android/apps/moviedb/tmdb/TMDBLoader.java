package com.edison.android.apps.moviedb.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

public abstract class TMDBLoader<T> extends AsyncTaskLoader<T> {

    private final TMDB mTmdb;
    private T mResult;

    public TMDBLoader(@NonNull Context context, @NonNull TMDB tmdb) {
        super(context);
        mTmdb = tmdb;
    }

    @Override
    protected void onStartLoading() {
        if (mResult == null) {
            forceLoad();
        } else {
            deliverResult(mResult);
        }
    }

    public abstract T result(@NonNull TMDB tmdb);

    @Override
    public T loadInBackground() {
        return result(mTmdb);
    }

    @Override
    public void deliverResult(T data) {
        mResult = data;
        super.deliverResult(data);
    }

}
