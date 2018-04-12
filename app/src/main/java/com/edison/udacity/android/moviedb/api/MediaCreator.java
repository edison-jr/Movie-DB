package com.edison.udacity.android.moviedb.api;

public interface MediaCreator<T> {

    T createFromReader(MediaReader reader);

}
