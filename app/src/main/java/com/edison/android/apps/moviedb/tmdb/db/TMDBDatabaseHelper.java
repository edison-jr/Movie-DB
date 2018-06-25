package com.edison.android.apps.moviedb.tmdb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TMDBDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "name";
    private static final int DB_VERSION = 1;

    public TMDBDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TMDBSchema.CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
