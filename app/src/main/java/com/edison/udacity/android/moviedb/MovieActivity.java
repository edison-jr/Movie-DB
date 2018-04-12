package com.edison.udacity.android.moviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.udacity.android.moviedb.api.media.BundleMedia;
import com.edison.udacity.android.moviedb.model.Movie;
import com.edison.udacity.android.moviedb.model.MovieReader;
import com.edison.udacity.android.moviedb.model.MovieWriter;
import com.edison.udacity.android.moviedb.model.Poster;
import com.edison.udacity.android.moviedb.tmdb.TMDBPoster;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MovieActivity extends AppCompatActivity {

    public static void start(@NonNull Context context, @NonNull Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        MovieWriter writer = new MovieWriter(movie);
        BundleMedia media = new BundleMedia(writer);
        intent.putExtras(media.media());
        context.startActivity(intent);
    }

    private ImageView mIvPoster;
    private TextView mTvOriginalTitle;
    private TextView mTvReleaseDate;
    private TextView mTvVoteAverage;
    private TextView mTvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setTitle(R.string.detail);

        mIvPoster = findViewById(R.id.iv_poster);
        mTvOriginalTitle = findViewById(R.id.tv_original_title);
        mTvReleaseDate = findViewById(R.id.tv_release_date);
        mTvVoteAverage = findViewById(R.id.tv_vote_average);
        mTvOverview = findViewById(R.id.tv_overview);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            Movie movie = new MovieReader(new BundleMedia(extras));
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
