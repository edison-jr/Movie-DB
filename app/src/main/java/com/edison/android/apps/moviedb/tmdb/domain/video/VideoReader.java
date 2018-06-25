package com.edison.android.apps.moviedb.tmdb.domain.video;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.edison.android.tools.Empty;
import com.edison.android.tools.media.MediaCreator;
import com.edison.android.tools.media.MediaReader;

public class VideoReader implements Video {

    private final MediaReader mMedia;

    public VideoReader(@NonNull MediaReader media) {
        mMedia = media;
    }

    @Override
    public String id() {
        return mMedia.readString(ID, Empty.STRING);
    }

    @Override
    public String key() {
        return mMedia.readString(KEY, Empty.STRING);
    }

    @Override
    public String name() {
        return mMedia.readString(NAME, Empty.STRING);
    }

    @Override
    public Uri site() {
        return Uri.parse(mMedia.readString(SITE, Empty.STRING));
    }

    public static class Creator implements MediaCreator<Video> {
        @Override
        public Video createFromReader(MediaReader reader) {
            return new VideoReader(reader);
        }
    }

}
