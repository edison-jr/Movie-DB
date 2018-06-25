package com.edison.android.apps.moviedb.tmdb.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.edison.android.apps.moviedb.tmdb.db.TMDBSchema.Movie;

public class TMDBProvider extends ContentProvider {

    private static final String AUTHORITY_URI = "com.edison.tmdb.provider";
    private static final String FAVORITE_MOVIES = "favorite_movies";

    private static final int MOVIES_URI_MATCH = 1;
    private static final int FAVORITE_MOVIES_URI_MATCH = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY_URI, Movie._TABLE, MOVIES_URI_MATCH);
        URI_MATCHER.addURI(AUTHORITY_URI, FAVORITE_MOVIES, FAVORITE_MOVIES_URI_MATCH);
    }

    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY_URI);

    static final Uri MOVIES_URI = BASE_URI.buildUpon().appendPath(Movie._TABLE).build();
    static final Uri FAVORITE_MOVIES_URI = BASE_URI.buildUpon().appendPath(FAVORITE_MOVIES).build();

    private TMDBDatabaseHelper mDbHelper;

    private TMDBDatabaseHelper dbHelper() {
        return mDbHelper;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new TMDBDatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == UriMatcher.NO_MATCH)
            throw new UnsupportedOperationException("No mach uri: " + uri);

        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = dbHelper().getWritableDatabase();
        return db.delete(tableName, selection, selectionArgs);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) == UriMatcher.NO_MATCH)
            throw new UnsupportedOperationException("No mach uri: " + uri);

        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = dbHelper().getWritableDatabase();
        long id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db;
        switch (URI_MATCHER.match(uri)) {
            case UriMatcher.NO_MATCH:
                throw new UnsupportedOperationException("No mach uri: " + uri);

            case FAVORITE_MOVIES_URI_MATCH:
                // Projection
                if (projection == null) {
                    projection = Movie._COLUMNS;
                }

                db = dbHelper().getReadableDatabase();
                return db.query(Movie._TABLE, projection, selection, selectionArgs, null, null, sortOrder);

            default: {
                String tableName = uri.getLastPathSegment();
                db = dbHelper().getReadableDatabase();
                return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
            }
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == UriMatcher.NO_MATCH)
            throw new UnsupportedOperationException("No mach uri: " + uri);

        String tableName = uri.getLastPathSegment();
        SQLiteDatabase db = dbHelper().getWritableDatabase();
        return db.update(tableName, values, selection, selectionArgs);
    }

}
