package com.edison.udacity.android.moviedb.api.media;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.edison.udacity.android.moviedb.api.MediaArray;
import com.edison.udacity.android.moviedb.api.MediaCreator;

import java.util.Iterator;

public class CursorMediaArray<T> implements MediaArray<T> {

    private static final String TAG = CursorMediaArray.class.getSimpleName();

    private final Cursor mMedia;
    private final MediaCreator<T> mCreator;

    public CursorMediaArray(@NonNull Cursor media, @NonNull MediaCreator<T> creator) {
        mMedia = media;
        mCreator = creator;
    }

    @Override
    public int size() {
        return mMedia.getCount();
    }

    @Override
    public T get(int position) {
        mMedia.moveToPosition(position);
        return mCreator.createFromReader(new CursorMedia(mMedia));
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new CursorMediaIterator();
    }

    class CursorMediaIterator implements Iterator<T> {

        private int mIndex;

        CursorMediaIterator() {
            mIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return mIndex < mMedia.getCount();
        }

        @Override
        public T next() {
            mMedia.moveToPosition(mIndex++);
            return mCreator.createFromReader(new CursorMedia(mMedia));
        }
    }

}
