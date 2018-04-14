package com.edison.android.tools.media.extras;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaReader;
import com.edison.android.tools.media.MediaWritable;
import com.edison.android.tools.media.MediaWriter;

/**
 * Created by jeziel on 11/03/17.
 */
public class BundleMedia implements MediaReader, MediaWriter<Bundle> {

    private static final String TAG = BundleMedia.class.getSimpleName();

    private Bundle mMedia;

    public BundleMedia() {
        this(new Bundle());
    }

    public BundleMedia(@NonNull Bundle media) {
        this.mMedia = media;
    }

    public BundleMedia(@NonNull MediaWritable media) {
        this();
        media.write(this);
    }

    @Override
    public String readString(@NonNull String name, String defaultValue) {
        return mMedia.getString(name, defaultValue);
    }

    @Override
    public int readInt(@NonNull String name, int defaultValue) {
        return mMedia.getInt(name, defaultValue);
    }

    @Override
    public double readDouble(@NonNull String name, double defaultValue) {
        return mMedia.getDouble(name, defaultValue);
    }

    @Override
    public boolean readBoolean(@NonNull String name, boolean defaultValue) {
        return mMedia.getBoolean(name, defaultValue);
    }

    @Override
    public long readLong(String name, long defaultValue) {
        return mMedia.getLong(name, defaultValue);
    }

    @Override
    public void writeString(@NonNull String name, String value) {
        mMedia.putString(name, value);
    }

    @Override
    public void writeInt(@NonNull String name, int value) {
        mMedia.putInt(name, value);
    }

    @Override
    public void writeDouble(@NonNull String name, double value) {
        mMedia.putDouble(name, value);
    }

    @Override
    public void writeBoolean(@NonNull String name, boolean value) {
        mMedia.putBoolean(name, value);
    }

    @Override
    public void writeLong(@NonNull String name, long value) {
        mMedia.putLong(name, value);
    }

    @Override
    public Bundle media() {
        return mMedia;
    }

}
