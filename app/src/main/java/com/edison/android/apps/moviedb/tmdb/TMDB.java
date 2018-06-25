package com.edison.android.apps.moviedb.tmdb;

import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.edison.android.apps.moviedb.tmdb.domain.review.Reviews;
import com.edison.android.apps.moviedb.tmdb.domain.video.Videos;

import java.io.IOException;

public interface TMDB {

    Movies movies() throws IOException;

    Videos videos() throws IOException;

    Reviews reviews() throws IOException;

}
