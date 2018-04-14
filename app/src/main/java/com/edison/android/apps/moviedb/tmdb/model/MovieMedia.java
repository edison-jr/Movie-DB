package com.edison.android.apps.moviedb.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MovieMedia implements Movie, Parcelable {

    private final String id;
    private final String originalTitle;
    private final String posterPath;
    private final String overview;
    private final Date releaseDate;
    private final double voteAverage;
    private final double popularity;

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

    public MovieMedia(Movie movie) {
        id = movie.id();
        originalTitle = movie.originalTitle();
        posterPath = movie.posterPath();
        overview = movie.overview();
        releaseDate = movie.releaseDate();
        voteAverage = movie.voteAverage();
        popularity = movie.popularity();
    }

    protected MovieMedia(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = new Date(in.readLong());
        voteAverage = in.readDouble();
        popularity = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new MovieMedia(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new MovieMedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeLong(releaseDate.getTime());
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
    }
}
