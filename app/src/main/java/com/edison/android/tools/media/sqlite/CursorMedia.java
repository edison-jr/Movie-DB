package com.edison.android.tools.media.sqlite;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaReader;

/**
 * Created by jeziel on 10/03/17.
 */
public class CursorMedia implements MediaReader {

    private Cursor mCursor;

    public CursorMedia(@NonNull Cursor cursor) {
        this.mCursor = cursor;
    }

    @Override
    public String readString(@NonNull String name, String defaultValue) {
        int index = mCursor.getColumnIndex(name);
        return index >= 0 ? mCursor.getString(index) : defaultValue;
    }

    @Override
    public int readInt(@NonNull String name, int defaultValue) {
        int index = mCursor.getColumnIndex(name);
        return index >= 0 ? mCursor.getInt(index) : defaultValue;
    }

    @Override
    public double readDouble(@NonNull String name, double defaultValue) {
        int index = mCursor.getColumnIndex(name);
        return index >= 0 ? mCursor.getDouble(index) : defaultValue;
    }

    @Override
    public boolean readBoolean(@NonNull String name, boolean defaultValue) {
        int index = mCursor.getColumnIndex(name);
        return index >= 0 ? mCursor.getInt(index) == 1 : defaultValue;
    }

    @Override
    public long readLong(@NonNull String name, long defaultValue) {
        int index = mCursor.getColumnIndex(name);
        return index >= 0 ? mCursor.getLong(index) : defaultValue;
    }
}
