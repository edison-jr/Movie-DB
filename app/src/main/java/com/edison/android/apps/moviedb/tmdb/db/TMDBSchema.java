package com.edison.android.apps.moviedb.tmdb.db;

import com.edison.android.tools.SQLiteType;

public interface TMDBSchema {

    interface Movie extends com.edison.android.apps.moviedb.tmdb.domain.movie.Movie {

        /**
         * Table name
         */
        String _TABLE = "movies";

        String[] _COLUMNS = {
                ID,
                ORIGINAL_TITLE,
                POSTER_PATH,
                OVERVIEW,
                RELEASE_DATE,
                VOTE_AVERAGE,
                POPULARITY,
                FAVORITE};

    }

    String CREATE_TABLE_MOVIES =
            "create table " + Movie._TABLE + " (" +
                    Movie.ID + " " + SQLiteType.INTEGER + " primary key," +
                    Movie.ORIGINAL_TITLE + " " + SQLiteType.TEXT + ", " +
                    Movie.POSTER_PATH + " " + SQLiteType.TEXT + ", " +
                    Movie.OVERVIEW + " " + SQLiteType.TEXT + ", " +
                    Movie.RELEASE_DATE + " " + SQLiteType.TEXT + ", " +
                    Movie.VOTE_AVERAGE + " " + SQLiteType.REAL + ", " +
                    Movie.POPULARITY + " " + SQLiteType.REAL + ", " +
                    Movie.FAVORITE + " " + SQLiteType.INTEGER + ")";

}
