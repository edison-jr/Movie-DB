package com.edison.android.tools.media;

import android.support.annotation.NonNull;

import java.util.Iterator;

public abstract class MediaArrayImp<T> implements MediaArray<T> {

    private final MediaArray<T> mItems;

    public MediaArrayImp(@NonNull MediaArray<T> items) {
        mItems = items;
    }

    public MediaArrayImp(@NonNull MediaArrayImp<T> array) {
        mItems = array.mItems;
    }

    @Override
    public int size() {
        return mItems.size();
    }

    @Override
    public T get(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return mItems.iterator();
    }

}
