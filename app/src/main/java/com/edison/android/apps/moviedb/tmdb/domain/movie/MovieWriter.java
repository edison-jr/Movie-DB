package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.support.annotation.NonNull;

import com.edison.android.tools.media.Media;
import com.edison.android.tools.media.MediaWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieWriter implements MediaWriter {

    private static final String DATE_PATTERN = "yyyy-mm-dd";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

    private final Movie mMovie;

    public MovieWriter(Movie movie) {
        mMovie = movie;
    }

    @Override
    public void into(@NonNull Media writer, int flags) {
        writer.writeString(Movie.ID, mMovie.id());
        writer.writeString(Movie.ORIGINAL_TITLE, mMovie.originalTitle());
        writer.writeString(Movie.OVERVIEW, mMovie.overview());
        writer.writeString(Movie.POSTER_PATH, mMovie.posterPath());
        writer.writeDouble(Movie.POPULARITY, mMovie.popularity());
        Date releaseDate = mMovie.releaseDate();
        writer.writeString(Movie.RELEASE_DATE, dateFormat.format(releaseDate));
        writer.writeDouble(Movie.VOTE_AVERAGE, mMovie.voteAverage());
        writer.writeInt(Movie.FAVORITE, mMovie.favorite() ? 1 : 0);
    }

}
