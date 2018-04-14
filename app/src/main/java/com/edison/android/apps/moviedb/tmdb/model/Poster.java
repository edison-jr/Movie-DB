package com.edison.android.apps.moviedb.tmdb.model;

import android.net.Uri;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Poster {

    String IMG_W_32 = "w92";
    String IMG_W_154 = "w154";
    String IMG_W_185 = "w185";
    String IMG_W_342 = "w342";
    String IMG_W_500 = "w500";
    String IMG_W_780 = "w780";
    String IMG_ORIGINAL = "original";

    @StringDef({IMG_W_32, IMG_W_154, IMG_W_185, IMG_W_342, IMG_W_500, IMG_W_780, IMG_ORIGINAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface ImageSize {}

    Uri poster(String imgPath);

}
