package com.edison.udacity.android.moviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.udacity.android.moviedb.model.Movie;
import com.edison.udacity.android.moviedb.model.MovieMedia;
import com.edison.udacity.android.moviedb.model.Poster;
import com.edison.udacity.android.moviedb.tmdb.TMDBPoster;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setTitle(R.string.detail);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            Movie movie = extras.getParcelable(MOVIE);
            assert movie != null;
            setMovie(movie);
        }
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
