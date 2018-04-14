package com.edison.android.apps.moviedb.tmdb.model;

import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaArray;

import java.util.Iterator;

public class Movies implements MediaArray<Movie> {

    private MediaArray<Movie> mMovies;

    public Movies(@NonNull MediaArray<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public int size() {
        return mMovies.size();
    }

    @Override
    public Movie get(int position) {
        return mMovies.get(position);
    }

    @NonNull
    @Override
    public Iterator<Movie> iterator() {
        return mMovies.iterator();
    }

}
