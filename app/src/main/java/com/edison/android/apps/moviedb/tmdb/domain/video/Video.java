package com.edison.android.apps.moviedb.tmdb.domain.video;

import android.net.Uri;

public interface Video {

    /**
     * Columns
     */
    String ID = "id";

    String KEY = "key";

    String NAME = "name";

    String SITE = "site";

    /**
     * @return id
     */
    String id();

    /**
     * @return key
     */
    String key();

    /**
     * @return Nome
     */
    String name();

    /**
     * @return site
     */
    Uri site();



}
