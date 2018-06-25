package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.TMDBLoader;

import java.io.IOException;

public class MoviesLoader extends TMDBLoader<Movies> {

    private static final String TAG = MoviesLoader.class.getSimpleName();

    public MoviesLoader(@NonNull Context context, @NonNull TMDB tmdb) {
        super(context, tmdb);
    }

    @Override
    public Movies result(@NonNull TMDB tmdb) {
        try {
            return tmdb.movies();
        } catch (IOException e) {
            Log.e(TAG, "Failure on get movies", e);
            return null;
        }
    }

}
