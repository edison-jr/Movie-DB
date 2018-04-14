package com.edison.android.tools.media;

public interface MediaArray<T> extends Iterable<T> {

    /**
     * @return the number of rows in the array.
     */
    int size();

    /**
     * Get item at position
     * @param position item position
     * @return item data
     */
    T get(int position);

}
