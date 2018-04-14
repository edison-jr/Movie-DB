package com.edison.android.apps.moviedb;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements TMDB.OnResult<Movies>, MovieAdapter.OnItemClickListener {

    private static final int IMG_SIZE = 185;

    private final NetworkListener mNetworkListener = new NetworkListener() {

        @Override
        public void onConnected() {
            mConnectionFailureMessage.setVisibility(View.GONE);
            popular();
        }

        @Override
        public void onConnectionFailure() {
            mConnectionFailureMessage.setVisibility(View.VISIBLE);
        }

    };

    private @StringRes int mTitle;

    @BindView(R.id.rv_movies)  RecyclerView mMovies;
    @BindView(R.id.tv_connection_failure_message)  TextView mConnectionFailureMessage;
    @BindView(R.id.tv_error_message)  TextView mErrorMessage;
    @BindView(R.id.pb_loading_indicator)  ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpanCount());
        mMovies.setLayoutManager(layoutManager);
        mMovies.setHasFixedSize(true);

        popular();
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
                popular();
                return true;
            case R.id.ac_top_rated:
                topRated();
                return true;
        }
        return false;
    }

    private void popular() {
        request(R.string.popular, TMDB.popular());
    }

    private void topRated() {
        request(R.string.top_rated, TMDB.topRated());
    }

    private void request(@StringRes int title, TMDB tmdb) {
        mTitle = title;
        tmdb.movies(this);
    }

    private void onResponse() {
        setTitle(mTitle);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(TMDB tmdb, IOException e) {
        onResponse();
        mErrorMessage.setVisibility(View.VISIBLE);
        mMovies.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResult(TMDB tmdb, Movies movies) {
        mMovies.setVisibility(View.VISIBLE);
        MovieAdapter adapter = new MovieAdapter(movies, new TMDBPoster(Poster.IMG_W_185), this);
        mMovies.setAdapter(adapter);
        mConnectionFailureMessage.setVisibility(View.GONE);
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
