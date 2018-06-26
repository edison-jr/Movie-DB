package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.os.Parcel;

public class MovieParcel {

    private final Movie mMovie;

    public MovieParcel(Movie movie) {
        mMovie = movie;
    }

    public void write(Parcel dest) {
        dest.writeString(mMovie.id());
        dest.writeString(mMovie.originalTitle());
        dest.writeString(mMovie.posterPath());
        dest.writeString(mMovie.overview());
        dest.writeLong(mMovie.releaseDate().getTime());
        dest.writeDouble(mMovie.voteAverage());
        dest.writeDouble(mMovie.popularity());
        dest.writeInt(mMovie.favorite() ? 1 : 0);
    }

}
