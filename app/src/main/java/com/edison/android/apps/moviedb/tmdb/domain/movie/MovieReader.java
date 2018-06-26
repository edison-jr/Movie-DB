package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.tools.Empty;
import com.edison.android.tools.media.MediaCreator;
import com.edison.android.tools.media.MediaReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieReader implements Movie {

    private static final String TAB = Movie.class.getSimpleName();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
    private final MediaReader mMedia;

    public MovieReader(@NonNull MediaReader media) {
        mMedia = media;
    }

    @Override
    public String id() {
        return mMedia.readString(ID, Empty.STRING);
    }

    @Override
    public String originalTitle() {
        return mMedia.readString(ORIGINAL_TITLE, Empty.STRING);
    }

    @Override
    public String posterPath() {
        return mMedia.readString(POSTER_PATH, Empty.STRING);
    }

    @Override
    public String overview() {
        return mMedia.readString(OVERVIEW, Empty.STRING);
    }

    @Override
    public Date releaseDate() {
        try {
            String releaseDate = mMedia.readString(RELEASE_DATE, Empty.STRING);
            return dateFormat.parse(releaseDate);
        } catch (ParseException e) {
            Log.e(TAB, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public double voteAverage() {
        return mMedia.readDouble(VOTE_AVERAGE, Empty.DOUBLE);
    }

    @Override
    public double popularity() {
        return mMedia.readDouble(POPULARITY, Empty.DOUBLE);
    }

    @Override
    public boolean favorite() {
        return mMedia.readBoolean(FAVORITE, Empty.BOOLEAN);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        new MovieParcel(this).write(dest);
    }

    public static class Creator implements MediaCreator<Movie> {
        @Override
        public Movie createFromReader(MediaReader reader) {
            return new MovieReader(reader);
        }
    }

}
