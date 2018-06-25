package com.edison.android.apps.moviedb.tmdb.domain.review;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.edison.android.tools.Empty;
import com.edison.android.tools.media.MediaCreator;
import com.edison.android.tools.media.MediaReader;

public class ReviewReader implements Review {

    private final MediaReader mMedia;

    public ReviewReader(@NonNull MediaReader media) {
        mMedia = media;
    }

    @Override
    public String id() {
        return mMedia.readString(ID, Empty.STRING);
    }

    @Override
    public String author() {
        return mMedia.readString(AUTHOR, Empty.STRING);
    }

    @Override
    public String content() {
        return mMedia.readString(CONTENT, Empty.STRING);
    }

    @Override
    public Uri url() {
        return Uri.parse(mMedia.readString(URL, Empty.STRING));
    }

    public static class Creator implements MediaCreator<Review> {
        @Override
        public Review createFromReader(MediaReader reader) {
            return new ReviewReader(reader);
        }
    }

}
