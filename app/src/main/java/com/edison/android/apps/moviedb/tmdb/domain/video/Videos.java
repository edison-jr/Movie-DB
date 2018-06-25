package com.edison.android.apps.moviedb.tmdb.domain.video;

import android.support.annotation.NonNull;

import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.MediaArrayImp;

public class Videos extends MediaArrayImp<Video> {

    public Videos(@NonNull MediaArray<Video> videos) {
        super(videos);
    }

    public Videos(@NonNull Videos videos) {
        super(videos);
    }

}
