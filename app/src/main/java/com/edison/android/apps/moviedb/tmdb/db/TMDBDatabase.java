package com.edison.android.apps.moviedb.tmdb.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.util.SparseArray;

import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MovieModel;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MoviesLoader;

public class TMDBDatabase {

    private final Context mContext;

    public TMDBDatabase(@NonNull Context context) {
        mContext = context;
    }

    public Loader<Movies> favorites() {
        ContentResolver resolver = mContext.getContentResolver();
        TMDB tmdb = new TMDBQuery(resolver, TMDBProvider.FAVORITE_MOVIES_URI);
        return new MoviesLoader(mContext, tmdb);
    }

    public SparseArray<String> favoritesId() {
        String[] projection = new String[] {Movie.ID};
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor =  resolver.query(TMDBProvider.FAVORITE_MOVIES_URI, projection,
                null, null, null);
        assert cursor != null;
        SparseArray<String> array = new SparseArray<>(cursor.getCount());
        int columnIndex = 0;
        while (cursor.moveToNext()) {
            String id = cursor.getString(columnIndex);
            array.put(id.hashCode(), id);
        }
        return array;
    }

    /**
     * Change movie favorite condition
     * @param source movie to change
     * @param favorite favorite condition
     * @return changed movie
     */
    public Movie favorite(Movie source, boolean favorite) {
        MovieModel movie = new MovieModel(source, favorite);
        if (movie.favorite()) {
            insert(movie);
        } else {
            delete(movie.id());
        }
        return movie;
    }

    public void insert(Movie movie) {
        TMDBInsert insert = new TMDBInsert(movie);
        insert.into(mContext);
    }

    public void delete(String id) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(TMDBProvider.MOVIES_URI, "id = ?", new String[] {id});
    }

}
