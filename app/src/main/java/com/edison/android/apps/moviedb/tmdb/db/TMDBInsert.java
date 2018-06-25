package com.edison.android.apps.moviedb.tmdb.db;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;

import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MovieWriter;
import com.edison.android.tools.media.MediaWriter;
import com.edison.android.tools.media.sqlite.ContentValuesMedia;

public class TMDBInsert {

    private final Movie mMovie;

    TMDBInsert(Movie movie) {
        mMovie = movie;
    }

    public void into(@NonNull Context context) {
        ContentValuesMedia media = new ContentValuesMedia();
        MediaWriter writer = new MovieWriter(mMovie);
        writer.into(media, MediaWriter.NO_FLAGS);

        ContentResolver resolver = context.getContentResolver();
        resolver.insert(TMDBProvider.MOVIES_URI, media.data());
    }

}
