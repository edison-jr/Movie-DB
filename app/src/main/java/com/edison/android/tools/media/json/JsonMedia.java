package com.edison.android.tools.media.json;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.tools.media.Media;
import com.edison.android.tools.media.MediaReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jeziel on 11/03/17.
 */
public class JsonMedia implements MediaReader, Media<JSONObject> {

    private static final String TAG = JsonMedia.class.getSimpleName();

    private JSONObject mMedia;

    public JsonMedia(@NonNull JSONObject media) {
        this.mMedia = media;
    }

    @Override
    public String readString(@NonNull String name, String defaultValue) {
        try {
            return (!mMedia.isNull(name)) ? mMedia.getString(name) : defaultValue;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public int readInt(@NonNull String name, int defaultValue) {
        try {
            return (!mMedia.isNull(name)) ? mMedia.getInt(name) : defaultValue;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    @Override
    public double readDouble(@NonNull String name, double defaultValue) {
        try {
            return (!mMedia.isNull(name)) ? mMedia.getDouble(name) : defaultValue;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean readBoolean(@NonNull String name, boolean defaultValue) {
        try {
            return (!mMedia.isNull(name)) ? mMedia.getBoolean(name) : defaultValue;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public long readLong(@NonNull String name, long defaultValue) {
        try {
            return (!mMedia.isNull(name)) ? mMedia.getLong(name) : defaultValue;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    @Override
    public void writeString(@NonNull String name, String value) {
        try {
            mMedia.put(name, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void writeInt(@NonNull String name, int value) {
        try {
            mMedia.put(name, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void writeDouble(@NonNull String name, double value) {
        try {
            mMedia.put(name, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void writeBoolean(@NonNull String name, boolean value) {
        try {
            mMedia.put(name, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void writeLong(@NonNull String name, long value) {
        try {
            mMedia.put(name, value);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public JSONObject data() {
        return mMedia;
    }
}
