package com.edison.android.tools.media;

public interface MediaCreator<T> {

    T createFromReader(MediaReader reader);

}
