package com.edison.android.apps.moviedb.tmdb.youtube;

import android.net.Uri;

import com.edison.android.apps.moviedb.tmdb.ImageLoader;

public class YoutubeThumbnail implements ImageLoader {

    private static final String THUMBNAIL_IMAGE_QUALITY = "hqdefault.jpg";
    private static final String THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";

    public static final YoutubeThumbnail DEFAULT = new YoutubeThumbnail(THUMBNAIL_IMAGE_QUALITY);

    private final Uri mBaseUri;
    private final String mImgQuality;

    private YoutubeThumbnail(String imgQuality) {
        mBaseUri = Uri.parse(THUMBNAIL_BASE_URL);
        mImgQuality = imgQuality;
    }

    @Override
    public Uri image(String imgPath) {
        return mBaseUri.buildUpon()
                .appendPath(imgPath)
                .appendPath(mImgQuality)
                .build();
    }

}
