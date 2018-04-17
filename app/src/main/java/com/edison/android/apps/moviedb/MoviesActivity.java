package com.edison.android.apps.moviedb;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

import com.edison.android.apps.moviedb.tmdb.api.TMDB;
import com.edison.android.apps.moviedb.tmdb.api.TMDBPoster;
import com.edison.android.apps.moviedb.tmdb.model.Movies;
import com.edison.android.apps.moviedb.tmdb.model.Poster;
import com.edison.android.tools.net.NetworkListener;

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
            request(R.string.popular);
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
        } else {
            request(R.string.popular);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkListener, filter);
    }

    @Override
    protected void onPause() {
        mConnectionFailureMessage.setVisibility(View.GONE);
        unregisterReceiver(mNetworkListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
        }
        return false;
    }

    private void request(@StringRes int id) {
        getSupportLoaderManager().restartLoader(id, null, this);
    }

    @Override
    public Loader<Movies> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.string.popular:
                return TMDB.popular().movies(this);
            case R.string.top_rated:
                return TMDB.topRated().movies(this);
        }
        throw new IllegalArgumentException("Not supported loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Movies> loader, Movies data) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (data == null) {
            mErrorMessage.setVisibility(View.VISIBLE);
            mMovies.setVisibility(View.INVISIBLE);
        } else {
            mCurrentLoaderId = loader.getId();
            setTitle(mCurrentLoaderId);
            mMovies.setVisibility(View.VISIBLE);
            MovieAdapter adapter = new MovieAdapter(data, new TMDBPoster(Poster.IMG_W_185), this);
            mMovies.setAdapter(adapter);
            mConnectionFailureMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movies> loader) {
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
        MovieActivity.start(this, adapter.getItem(position));
    }

    private int getSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        return Math.round(dpWidth/IMG_SIZE);
    }

}
