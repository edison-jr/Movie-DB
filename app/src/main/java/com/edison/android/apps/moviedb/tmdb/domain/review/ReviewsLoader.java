package com.edison.android.apps.moviedb.tmdb.domain.review;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.TMDBLoader;

import java.io.IOException;

public class ReviewsLoader extends TMDBLoader<Reviews> {

    private static final String TAG = ReviewsLoader.class.getSimpleName();

    public ReviewsLoader(@NonNull Context context, @NonNull TMDB tmdb) {
        super(context, tmdb);
    }

    @Override
    public Reviews result(@NonNull TMDB tmdb) {
        try {
            return tmdb.reviews();
        } catch (IOException e) {
            Log.e(TAG, "Failure on get reviews", e);
            return null;
        }
    }
}
