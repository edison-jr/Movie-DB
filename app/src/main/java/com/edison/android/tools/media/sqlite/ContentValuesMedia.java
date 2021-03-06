package com.edison.android.tools.media.sqlite;


import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.edison.android.tools.media.Media;

/**
 * Created by jeziel on 10/03/17.
 */
public class ContentValuesMedia implements Media<ContentValues> {

    private ContentValues mValues;

    public ContentValuesMedia(@NonNull ContentValues values) {
        this.mValues = values;
    }

    public ContentValuesMedia() {
        this(new ContentValues());
    }

    @Override
    public void writeString(@NonNull String name, String value) {
        mValues.put(name, value);
    }

    @Override
    public void writeInt(@NonNull String name, int value) {
        mValues.put(name, value);
    }

    @Override
    public void writeDouble(@NonNull String name, double value) {
        mValues.put(name, value);
    }

    @Override
    public void writeBoolean(@NonNull String name, boolean value) {
        mValues.put(name, value);
    }

    @Override
    public void writeLong(@NonNull String name, long value) {
        mValues.put(name, value);
    }

    @Override
    public ContentValues data() {
        return mValues;
    }

}
