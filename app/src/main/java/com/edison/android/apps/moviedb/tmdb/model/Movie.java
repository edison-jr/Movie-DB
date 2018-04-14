package com.edison.android.apps.moviedb.tmdb.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.tools.Empty;
import com.edison.android.tools.media.MediaCreator;
import com.edison.android.tools.media.MediaReader;
import com.edison.android.tools.media.MediaWritable;
import com.edison.android.tools.media.MediaWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public interface Movie {

    /**
     * @return id
     */
    String id();

    /**
     * @return Título original
     */
    String originalTitle();

    /**
     * @return Caminho do poster
     */
    String posterPath();

    /**
     * @return Sinopse
     */
    String overview();

    /**
     * @return Data de lançamento
     */
    Date releaseDate();

    /**
     * @return Avaliação dos usuários
     */
    double voteAverage();

    /**
     * @return Popularidade
     */
    double popularity();

    class Writer implements MediaWritable {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        private final Movie mMovie;

        public Writer(Movie movie) {
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

    class Reader implements Movie {

        private static final String TAB = Movie.class.getSimpleName();

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        private MediaReader mMedia;

        public Reader(@NonNull MediaReader media) {
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

    class Creator implements MediaCreator<Movie> {

        @Override
        public Movie createFromReader(MediaReader reader) {
            return new Movie.Reader(reader);
        }

    }


}
