package com.edison.android.apps.moviedb.tmdb.domain.review;

import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.MediaArrayImp;

public class Reviews extends MediaArrayImp<Review> {

    public Reviews(@NonNull MediaArray<Review> reviews) {
        super(reviews);
    }

    public Reviews(@NonNull Reviews reviews) {
        super(reviews);
    }

}
