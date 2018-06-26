package com.edison.android.apps.moviedb;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.api.TMDBApi;
import com.edison.android.apps.moviedb.tmdb.api.TMDBPoster;
import com.edison.android.apps.moviedb.tmdb.db.TMDBDatabase;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.edison.android.tools.net.NetworkListener;
import com.edison.android.tools.net.VerifyConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Movies> {

    private static final int IMG_SIZE = 185;

    private static final String LOADER_ID = "loader_id";

    private final NetworkListener mNetworkListener = new NetworkListener() {

        @Override
        public void onConnected() {
            mConnectionFailureMessage.setVisibility(View.GONE);
            request(mCurrentLoaderId);
        }

        @Override
        public void onConnectionFailure() {
            mConnectionFailureMessage.setVisibility(View.VISIBLE);
        }

    };

    @BindView(R.id.rv_movies)  RecyclerView mMovies;
    @BindView(R.id.tv_connection_failure_message)  TextView mConnectionFailureMessage;
    @BindView(R.id.tv_error_message)  TextView mErrorMessage;
    @BindView(R.id.pb_loading_indicator)  ProgressBar mLoadingIndicator;

    @StringRes int mCurrentLoaderId;
    MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpanCount());
        mMovies.setLayoutManager(layoutManager);
        mMovies.setHasFixedSize(true);

        Bundle extras;
        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else {
            extras = getIntent().getExtras();
        }

        if (extras != null) {
            request(extras.getInt(LOADER_ID, R.string.popular));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkListener, filter);

        invalidate();

        if (new VerifyConnection(this).connected()) {
            mConnectionFailureMessage.setVisibility(View.GONE);
        } else {
            mConnectionFailureMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mNetworkListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ac_popular:
                request(R.string.popular);
                return true;
            case R.id.ac_top_rated:
                request(R.string.top_rated);
                return true;
            case R.id.ac_favorites:
                request(R.string.favorites);
                return true;
        }
        return false;
    }

    private void request(@StringRes int id) {
        if (id == 0) {
            id = R.string.popular;
        }
        getSupportLoaderManager().restartLoader(id, null, this);
    }

    @NonNull
    @Override
    public Loader<Movies> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.string.popular:
                return TMDBApi.popular(this);
            case R.string.top_rated:
                return TMDBApi.topRated(this);
            case R.string.favorites:
                return new TMDBDatabase(this).favorites();
        }
        throw new IllegalArgumentException("Not supported loader id: " + id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movies> loader, Movies data) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (data == null || data.size() == 0) {
            mErrorMessage.setVisibility(View.VISIBLE);
            mMovies.setVisibility(View.INVISIBLE);
        } else {
            mCurrentLoaderId = loader.getId();
            setTitle(mCurrentLoaderId);
            mMovies.setVisibility(View.VISIBLE);
            mMovieAdapter = new MovieAdapter(data, TMDBPoster.IMG_W_185, this);
            mMovies.setAdapter(mMovieAdapter);
            mErrorMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movies> loader) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentLoaderId != 0) {
            outState.putInt(LOADER_ID, mCurrentLoaderId);
        }
    }

    @Override
    public void onItemClick(MovieAdapter adapter, int position) {
        Intent intent = MovieActivity.intent(this, adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onFavoriteItemClick(MovieAdapter adapter, int position) {
        Movie movie = adapter.getItem(position);
        TMDBDatabase db = new TMDBDatabase(this);
        db.favorite(movie, !movie.favorite());
        invalidate();
    }

    private void invalidate() {
        if (mCurrentLoaderId == R.string.favorites) {
            request(mCurrentLoaderId);
        } else if (mCurrentLoaderId > 0) {
            TMDBDatabase db = new TMDBDatabase(this);
            Movies movies = new TMDBApi.MoviesApi(mMovieAdapter.getMovies(), db.favoritesId());
            mMovieAdapter.setMovies(movies);
        }
    }

    private int getSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth/IMG_SIZE);
    }

}
