package com.edison.android.apps.moviedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.api.TMDBApi;
import com.edison.android.apps.moviedb.tmdb.api.TMDBPoster;
import com.edison.android.apps.moviedb.tmdb.db.TMDBDatabase;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.review.Review;
import com.edison.android.apps.moviedb.tmdb.domain.review.Reviews;
import com.edison.android.apps.moviedb.tmdb.domain.video.Video;
import com.edison.android.apps.moviedb.tmdb.domain.video.Videos;
import com.edison.android.apps.moviedb.tmdb.youtube.YoutubeThumbnail;
import com.edison.android.apps.moviedb.tmdb.youtube.YoutubeWatch;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener
        , ReviewAdapter.OnItemClickListener {

    private static final String MOVIE = "movie";
    private static final String YEAR_PATTERN ="yyyy";

    private static final int VIDEO_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;

    public static Intent intent(@NonNull Context context, @NonNull Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(MOVIE, movie);
        return intent;
    }

    private final SimpleDateFormat yearFormat = new SimpleDateFormat(YEAR_PATTERN, Locale.getDefault());

    private Movie mMovie;

    @BindView(R.id.iv_poster) ImageView mIvPoster;
    @BindView(R.id.tv_original_title) TextView mTvOriginalTitle;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_vote_average) TextView mTvVoteAverage;
    @BindView(R.id.tv_overview) TextView mTvOverview;
    @BindView(R.id.tv_videos) TextView mTvVideos;
    @BindView(R.id.rv_videos) RecyclerView mVideos;
    @BindView(R.id.tv_reviews) TextView mTvReviews;
    @BindView(R.id.rv_reviews) RecyclerView mReviews;

    MenuItem mIcFavorite;

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

        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mVideos.setLayoutManager(videoLayoutManager);
        mVideos.setHasFixedSize(true);

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviews.setLayoutManager(reviewLayoutManager);
        mReviews.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(VIDEO_LOADER_ID, null, new VideosLoader());
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, new ReviewsLoader());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        mIcFavorite = menu.findItem(R.id.ac_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ac_favorite:
                setFavoriteMovie();
                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setFavoriteIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setFavoriteMovie() {
        TMDBDatabase db = new TMDBDatabase(this);
        mMovie = db.favorite(mMovie, !mMovie.favorite());
        setFavoriteIcon();
        setResult(RESULT_OK);
    }

    private void setFavoriteIcon() {
        mIcFavorite.setIcon(mMovie.favorite() ? R.drawable.favorite_white_24 : R.drawable.favorite_border_white_24);
    }

    private void setMovie(@NonNull Movie movie) {
        Picasso.get().load(TMDBPoster.IMG_W_154.image(movie.posterPath())).into(mIvPoster);
        mTvOriginalTitle.setText(movie.originalTitle());
        mTvReleaseDate.setText(yearFormat.format(movie.releaseDate()));
        mTvVoteAverage.setText(String.valueOf(movie.voteAverage()));
        mTvOverview.setText(movie.overview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE, mMovie);
    }

    @Override
    public void onItemClick(VideoAdapter adapter, int position, Video video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, new YoutubeWatch().uri(video.key()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(ReviewAdapter adapter, int position, Review review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, review.url());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private class VideosLoader implements LoaderManager.LoaderCallbacks<Videos> {

        @NonNull
        @Override
        public Loader<Videos> onCreateLoader(int id, @Nullable Bundle args) {
            return TMDBApi.videos(MovieActivity.this, mMovie.id());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Videos> loader, Videos data) {
            if (data != null && data.size() > 0) {
                mTvVideos.setVisibility(View.VISIBLE);
                VideoAdapter adapter = new VideoAdapter(data, YoutubeThumbnail.DEFAULT, MovieActivity.this);
                mVideos.setAdapter(adapter);
            } else {
                mTvVideos.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Videos> loader) {
        }

    }

    private class ReviewsLoader implements LoaderManager.LoaderCallbacks<Reviews> {

        @NonNull
        @Override
        public Loader<Reviews> onCreateLoader(int id, @Nullable Bundle args) {
            return TMDBApi.reviws(MovieActivity.this, mMovie.id());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Reviews> loader, Reviews data) {
            if (data != null && data.size() > 0) {
                mTvReviews.setVisibility(View.VISIBLE);
                ReviewAdapter adapter = new ReviewAdapter(data, MovieActivity.this);
                mReviews.setAdapter(adapter);
            } else {
                mTvReviews.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Reviews> loader) {
        }

    }

}
