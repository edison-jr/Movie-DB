package com.edison.udacity.android.moviedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edison.udacity.android.moviedb.model.Movies;
import com.edison.udacity.android.moviedb.model.Poster;
import com.edison.udacity.android.moviedb.tmdb.TMDB;
import com.edison.udacity.android.moviedb.tmdb.TMDBPoster;

import java.io.IOException;

import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener{

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private class MoviesAsyncTask extends AsyncTask<TMDB, Void, Movies> {

        private @StringRes int mTitle;

        public MoviesAsyncTask(@StringRes int titlee) {
            mTitle = titlee;
        }

        @Override
        protected void onPreExecute() {
            mErrorMessage.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movies doInBackground(TMDB... params) {
            TMDB tmdb = params[0];
            try {
                return tmdb.movies();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movies movies) {
            mLoadingIndicator.setVisibility(View.GONE);
            setMovies(mTitle, movies);
        }

    }

    private final BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connected = !(intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY , false));
            if (mConnected != connected) {
                verifyConnection();
            }
        }
    };

    private RecyclerView mMovies;
    private TextView mConnectionFailureMessage;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private boolean mConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mConnectionFailureMessage = findViewById(R.id.tv_connection_failure_message);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMovies = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpanCount());
        mMovies.setLayoutManager(layoutManager);
        mMovies.setHasFixedSize(true);

        verifyConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectionReceiver, filter);
    }

    @Override
    protected void onPause() {
        mConnectionFailureMessage.setVisibility(View.GONE);
        unregisterReceiver(mConnectionReceiver);
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
                request(R.string.popular, TMDB.popular());
                return true;
            case R.id.ac_top_rated:
                request(R.string.top_rated, TMDB.topRated());
                return true;
        }
        return false;
    }

    private boolean isConnected() {
        return mConnected;
    }

    private void setConnected(boolean connected) {
        boolean oldState = mConnected;
        mConnected = connected;
        if (connected) {
            mConnectionFailureMessage.setVisibility(View.GONE);
            if (!oldState) {
                request(R.string.popular, TMDB.popular());
            }
        } else {
            mConnectionFailureMessage.setVisibility(View.VISIBLE);
        }
    }

    private boolean verifyConnection() {
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            connected = (activeNetwork != null && activeNetwork.isConnected());
        }
        setConnected(connected);
        return connected;
    }

    private void request(@StringRes int title, TMDB tmdb) {
        if (isConnected()) {
            new MoviesAsyncTask(title).execute(tmdb);
        }
    }

    private void setMovies(@StringRes int title, Movies movies) {
        if (movies == null) {
            mErrorMessage.setText(R.string.no_result);
            mErrorMessage.setVisibility(View.VISIBLE);
            mMovies.setVisibility(View.INVISIBLE);
        } else {
            setTitle(title);
            mMovies.setVisibility(View.VISIBLE);
            MovieAdapter adapter = new MovieAdapter(movies, new TMDBPoster(Poster.IMG_W_185), this);
            mMovies.setAdapter(adapter);
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
        return Math.round(dpWidth/185);
    }

    
}
