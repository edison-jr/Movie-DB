package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MovieModel implements Movie, Parcelable {

    private final String id;
    private final String originalTitle;
    private final String posterPath;
    private final String overview;
    private final Date releaseDate;
    private final double voteAverage;
    private final double popularity;
    private final boolean favorite;

    public MovieModel(Movie movie) {
        this(movie, movie.favorite());
    }

    public MovieModel(Movie movie, boolean favorite) {
        id = movie.id();
        originalTitle = movie.originalTitle();
        posterPath = movie.posterPath();
        overview = movie.overview();
        releaseDate = movie.releaseDate();
        voteAverage = movie.voteAverage();
        popularity = movie.popularity();
        this.favorite = favorite;
    }

    protected MovieModel(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = new Date(in.readLong());
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        favorite = (in.readInt() == 1);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String originalTitle() {
        return originalTitle;
    }

    @Override
    public String posterPath() {
        return posterPath;
    }

    @Override
    public String overview() {
        return overview;
    }

    @Override
    public Date releaseDate() {
        return releaseDate;
    }

    @Override
    public double voteAverage() {
        return voteAverage;
    }

    @Override
    public double popularity() {
        return popularity;
    }

    @Override
    public boolean favorite() {
        return favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        new MovieParcel(this).write(dest);
    }

    public static class Creator implements Parcelable.Creator<Movie> {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new MovieModel[size];
        }

    }

}
