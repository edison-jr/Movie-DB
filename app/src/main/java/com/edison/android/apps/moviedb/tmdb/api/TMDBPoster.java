package com.edison.android.apps.moviedb.tmdb.api;

import android.net.Uri;

import com.edison.android.apps.moviedb.tmdb.model.Poster;

public class TMDBPoster implements Poster {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private final @ImageSize String mImgSize;

    public TMDBPoster(@ImageSize String imgSize) {
        mImgSize = imgSize;
    }

    @Override
    public Uri poster(String imgPath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(mImgSize)
                .appendEncodedPath(imgPath)
                .build();
    }

}
