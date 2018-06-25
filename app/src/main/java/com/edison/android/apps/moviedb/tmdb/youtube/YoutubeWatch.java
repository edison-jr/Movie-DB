package com.edison.android.apps.moviedb.tmdb.youtube;

import android.net.Uri;

public class YoutubeWatch {

    private static final String WATCH_BASE_URL = "https://www.youtube.com/watch?v=";

    private static final String VIDEO_PARAM = "v";

    private final Uri mBaseUri = Uri.parse(WATCH_BASE_URL);

    public Uri uri(String key) {
        return mBaseUri.buildUpon()
                .appendQueryParameter(VIDEO_PARAM, key)
                .build();
    }

}
