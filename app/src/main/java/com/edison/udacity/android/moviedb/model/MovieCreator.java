package com.edison.udacity.android.moviedb.model;

import com.edison.udacity.android.moviedb.api.MediaCreator;
import com.edison.udacity.android.moviedb.api.MediaReader;

public class MovieCreator implements MediaCreator<Movie> {

    @Override
    public Movie createFromReader(MediaReader reader) {
        return new MovieReader(reader);
    }

}
