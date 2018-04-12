package com.edison.udacity.android.moviedb.model;

import com.edison.udacity.android.moviedb.api.MediaWritable;
import com.edison.udacity.android.moviedb.api.MediaWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieWriter implements MediaWritable {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
    private final Movie mMovie;

    public MovieWriter(Movie movie) {
        mMovie = movie;
    }

    @Override
    public void write(MediaWriter writer) {
        writer.writeString("id", mMovie.id());
        writer.writeString("original_title", mMovie.originalTitle());
        writer.writeString("overview", mMovie.overview());
        writer.writeString("poster_path", mMovie.posterPath());
        writer.writeDouble("popularity", mMovie.popularity());
        Date releaseDate = mMovie.releaseDate();
        writer.writeString("release_date", dateFormat.format(releaseDate));
        writer.writeDouble("vote_average", mMovie.voteAverage());
    }

}
