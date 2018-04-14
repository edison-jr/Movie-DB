package com.edison.android.tools.media.json;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.MediaCreator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;

public class JsonMediaArray<T> implements MediaArray<T> {

    private static final String TAG = JsonMediaArray.class.getSimpleName();

    private final JSONArray mMedia;
    private final MediaCreator<T> mCreator;

    public JsonMediaArray(@NonNull JSONArray media, @NonNull MediaCreator<T> creator) {
        mMedia = media;
        mCreator = creator;
    }

    @Override
    public int size() {
        return mMedia.length();
    }

    @Override
    public T get(int position) {
        try {
            return mCreator.createFromReader(new JsonMedia(mMedia.getJSONObject(position)));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new JsonMediaIterator();
    }

    class JsonMediaIterator implements Iterator<T> {

        private int mIndex;

        JsonMediaIterator() {
            mIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return (mIndex < mMedia.length());
        }

        @Override
        public T next() {
            return get(mIndex++);
        }
    }

}
