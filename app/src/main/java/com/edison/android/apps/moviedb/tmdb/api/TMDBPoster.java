package com.edison.android.apps.moviedb.tmdb.api;

import android.net.Uri;

import com.edison.android.apps.moviedb.tmdb.ImageLoader;

public class TMDBPoster implements ImageLoader {

    private static final String W_154 = "w154";
    private static final String W_185 = "w185";

    public static final ImageLoader IMG_W_154 = new TMDBPoster(W_154);
    public static final ImageLoader IMG_W_185 = new TMDBPoster(W_185);

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private final Uri mBaseUri;

    private TMDBPoster(String imgSize) {
        mBaseUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imgSize)
                .build();
    }

    @Override
    public Uri image(String imgPath) {
        return mBaseUri.buildUpon()
                .appendEncodedPath(imgPath)
                .build();
    }

}
