package com.edison.android.apps.moviedb.tmdb.domain.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.TMDBLoader;

import java.io.IOException;

public class VideosLoader extends TMDBLoader<Videos> {

    private static final String TAG = VideosLoader.class.getSimpleName();

    public VideosLoader(@NonNull Context context, @NonNull TMDB tmdb) {
        super(context, tmdb);
    }

    @Override
    public Videos result(@NonNull TMDB tmdb) {
        try {
            return tmdb.videos();
        } catch (IOException e) {
            Log.e(TAG, "Failure on get videos", e);
            return null;
        }
    }

}
