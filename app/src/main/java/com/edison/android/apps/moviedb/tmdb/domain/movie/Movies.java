package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.MediaArrayImp;

public class Movies extends MediaArrayImp<Movie> {

    public Movies(@NonNull MediaArray<Movie> movies) {
        super(movies);
    }

    public Movies(@NonNull Movies movies) {
        super(movies);
    }

}
