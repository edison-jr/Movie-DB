package com.edison.udacity.android.moviedb;

import android.os.AsyncTask;
import android.os.Bundle;
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

import com.edison.udacity.android.moviedb.model.Movie;
import com.edison.udacity.android.moviedb.model.Movies;
import com.edison.udacity.android.moviedb.model.Poster;
import com.edison.udacity.android.moviedb.tmdb.TMDB;
import com.edison.udacity.android.moviedb.tmdb.TMDBPoster;

import java.io.IOException;

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener{

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private class MoviesAsyncTask extends AsyncTask<TMDB, Void, Movies> {

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
            setMovies(movies);
        }

    }

    private class OnMovieItemClick implements MovieAdapter.OnItemClickListener {

        @Override
        public void onItemClick(MovieAdapter adapter, int position) {
            Movie movie = adapter.getItem(position);
            MovieActivity.start(MoviesActivity.this, movie);
        }

    }

    private RecyclerView mMovies;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mErrorMessage = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMovies = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpanCount());
        mMovies.setLayoutManager(layoutManager);
        mMovies.setHasFixedSize(true);

        setTitle(R.string.popular);
        new MoviesAsyncTask().execute(TMDB.popular());
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
                setTitle(R.string.popular);
                new MoviesAsyncTask().execute(TMDB.popular());
                return true;
            case R.id.ac_top_rated:
                setTitle(R.string.top_rated);
                new MoviesAsyncTask().execute(TMDB.topRated());
                return true;
        }
        return false;
    }

    private void setMovies(Movies movies) {
        if (movies == null) {
            mErrorMessage.setVisibility(View.VISIBLE);
            mMovies.setVisibility(View.INVISIBLE);
        } else {
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
