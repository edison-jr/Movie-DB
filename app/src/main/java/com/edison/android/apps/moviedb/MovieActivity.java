package com.edison.android.apps.moviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.api.TMDBPoster;
import com.edison.android.apps.moviedb.tmdb.model.Movie;
import com.edison.android.apps.moviedb.tmdb.model.MovieMedia;
import com.edison.android.apps.moviedb.tmdb.model.Poster;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {

    private static final String MOVIE = "movie";

    public static void start(@NonNull Context context, @NonNull Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(MOVIE, new MovieMedia(movie));
        context.startActivity(intent);
    }

    @BindView(R.id.iv_poster) ImageView mIvPoster;
    @BindView(R.id.tv_original_title)  TextView mTvOriginalTitle;
    @BindView(R.id.tv_release_date)  TextView mTvReleaseDate;
    @BindView(R.id.tv_vote_average)  TextView mTvVoteAverage;
    @BindView(R.id.tv_overview)  TextView mTvOverview;

    private MovieMedia mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        Bundle extras;
        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else {
            extras = getIntent().getExtras();
            assert extras != null;
        }
        mMovie = extras.getParcelable(MOVIE);
        assert mMovie != null;
        setMovie(mMovie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE, mMovie);
    }

    private void setMovie(@NonNull Movie movie) {
        Poster poster = new TMDBPoster(Poster.IMG_W_154);
        Picasso.get().load(poster.poster(movie.posterPath())).into(mIvPoster);
        mTvOriginalTitle.setText(movie.originalTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        mTvReleaseDate.setText(dateFormat.format(movie.releaseDate()));
        mTvVoteAverage.setText(String.valueOf(movie.voteAverage()));
        mTvOverview.setText(movie.overview());
    }
}
