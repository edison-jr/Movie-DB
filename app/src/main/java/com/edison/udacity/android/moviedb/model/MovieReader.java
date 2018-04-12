package com.edison.udacity.android.moviedb.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.udacity.android.moviedb.api.Empty;
import com.edison.udacity.android.moviedb.api.MediaReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieReader implements Movie {

    private static final String TAB = MovieReader.class.getSimpleName();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
    private MediaReader mMedia;

    public MovieReader(@NonNull MediaReader media) {
        mMedia = media;
    }

    @Override
    public String id() {
        return mMedia.readString("id", Empty.STRING);
    }

    @Override
    public String originalTitle() {
        return mMedia.readString("original_title", Empty.STRING);
    }

    @Override
    public String posterPath() {
        return mMedia.readString("poster_path", Empty.STRING);
    }

    @Override
    public String overview() {
        return mMedia.readString("overview", Empty.STRING);
    }

    @Override
    public Date releaseDate() {
        try {
            String releaseDate = mMedia.readString("release_date", Empty.STRING);
            return dateFormat.parse(releaseDate);
        } catch (ParseException e) {
            Log.e(TAB, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public double voteAverage() {
        return mMedia.readDouble("vote_average", Empty.DOUBLE);
    }

    @Override
    public double popularity() {
        return mMedia.readDouble("popularity", Empty.DOUBLE);
    }

}
