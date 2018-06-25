package com.edison.android.apps.moviedb.tmdb.domain.review;

import android.net.Uri;

public interface Review {

    /**
     * Columns
     */
    String ID = "id";

    String AUTHOR = "author";

    String CONTENT = "content";

    String URL = "url";

    /**
     * @return id
     */
    String id();

    /**
     * @return Autor
     */
    String author();

    /**
     * @return Crítica
     */
    String content();

    /**
     * @return url
     */
    Uri url();

}
