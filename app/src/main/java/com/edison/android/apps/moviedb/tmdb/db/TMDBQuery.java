package com.edison.android.apps.moviedb.tmdb.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MovieReader;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.edison.android.apps.moviedb.tmdb.domain.review.Reviews;
import com.edison.android.apps.moviedb.tmdb.domain.video.Videos;
import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.sqlite.CursorMediaArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TMDBQuery implements TMDB {

    private final ContentResolver mResolver;
    private final Uri mUri;

    TMDBQuery(ContentResolver resolver, Uri uri) {
        mResolver = resolver;
        mUri = uri;
    }

    @Override
    public Movies movies() throws IOException {
        Cursor cursor =  mResolver.query(mUri, null, null, null, null);
        assert cursor != null;
        MediaArray<Movie> media = new CursorMediaArray<>(cursor, new MovieReader.Creator());
        return new Movies(media);
    }

    @Override
    public Videos videos() throws IOException {
        throw new UnsupportedEncodingException("Not implemented yet");
    }

    @Override
    public Reviews reviews() throws IOException {
        throw new UnsupportedEncodingException("Not implemented yet");
    }

}
