package com.edison.android.apps.moviedb.tmdb.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.edison.android.apps.moviedb.BuildConfig;
import com.edison.android.apps.moviedb.tmdb.model.Movie;
import com.edison.android.apps.moviedb.tmdb.model.Movies;
import com.edison.android.tools.io.JsonResponse;
import com.edison.android.tools.media.json.JsonMedia;
import com.edison.android.tools.media.json.JsonMediaArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpRetryException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TMDB {

    private static final String TAG = TMDB.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String PARAM_API_KEY = "api_key";

    private static final String POPULAR_URL = "popular";
    private static final String TOP_RATED_URL = "top_rated";

    @StringDef({POPULAR_URL, TOP_RATED_URL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UrlPath {}

    public static TMDB popular() {
        return new TMDB().path(POPULAR_URL);
    }

    public static TMDB topRated() {
        return new TMDB().path(TOP_RATED_URL);
    }

    private final OkHttpClient mHttpClient;
    private final HttpUrl.Builder mUrl;

    public TMDB() {
        mHttpClient = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(BASE_URL);
        assert url != null;
        mUrl = url.newBuilder()
                .addQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY);
    }

    public TMDB path(@UrlPath String url) {
        mUrl.addPathSegment(url);
        return this;
    }

    public Loader<Movies> movies(@NonNull Context context) {
        return new MoviesLoader(context, this);
    }

    public void movies(OnResult<Movies> onResult) {
        MoviesCallback callback = new MoviesCallback(this, Looper.getMainLooper(), onResult);
        request().enqueue(callback);
    }

    public Movies movies() throws IOException {
        Response response = request().execute();
        MoviesResponse moviesResponse = new MoviesResponse(response);
        return moviesResponse.movies();
    }

    private Call request() {
        Request request = new Request.Builder()
                .url(mUrl.build())
                .build();
        return mHttpClient.newCall(request);
    }

    private static class MovieResponse {

        final Movie mMovie;

        MovieResponse(Response response) throws IOException {
            JsonResponse jsonResponse = new JsonResponse(response);
            JsonMedia media = new JsonMedia(jsonResponse.json());
            mMovie = new Movie.Reader(media);
        }

        public Movie movie() {
            return mMovie;
        }
    }

    private static class MoviesResponse {

        final Movies mMovies;

        MoviesResponse(Response response) throws IOException {
            JsonResponse jsonResponse = new JsonResponse(response);
            try {
                JSONArray results = jsonResponse.json().getJSONArray("results");
                mMovies = new Movies(new JsonMediaArray<>(results, new Movie.Creator()));
            } catch (JSONException e) {
                throw new HttpRetryException("Malformed results: " + response, response.code());
            }
        }

        public Movies movies() {
            return mMovies;
        }
    }

    private static class MoviesCallback implements Callback {

        final TMDB mTmdb;
        final OnResult<Movies> mResult;
        final Handler mHandler;

        MoviesCallback(TMDB tmdb, Looper looper, OnResult<Movies> result) {
            mTmdb = tmdb;
            mHandler = new Handler(looper);
            mResult = result;
        }

        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Log.e(TAG, "Failure on get movies", e);
            publish(new PublishFailure(mResult, mTmdb, e));
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            try {
                MoviesResponse moviesResponse = new MoviesResponse(response);
                publish(new PublishResult<>(mResult, mTmdb, moviesResponse.movies()));
            } catch (IOException e) {
                onFailure(call, e);
            }
        }

        private void publish(Runnable publisher) {
            mHandler.post(publisher);
        }
    }

    private static class PublishResult<Result> implements Runnable {
        final OnResult<Result> mResult;
        final TMDB mTmdb;
        final Result mData;

        PublishResult(OnResult<Result> result, TMDB tmdb, Result data) {
            mResult = result;
            mTmdb = tmdb;
            mData = data;
        }

        @Override
        public void run() {
            mResult.onResult(mTmdb, mData);
        }
    }

    private static class PublishFailure implements Runnable {
        final OnResult<?> mResult;
        final TMDB mTmdb;
        final IOException mEx;

        PublishFailure(OnResult<?> result, TMDB tmdb, IOException ex) {
            mResult = result;
            mTmdb = tmdb;
            mEx = ex;
        }

        @Override
        public void run() {
            mResult.onFailure(mTmdb, mEx);
        }
    }

    public interface OnResult<T> {
        void onFailure(TMDB tmdb, IOException e);

        void onResult(TMDB tmdb, T t);
    }

    private static class MoviesLoader extends AsyncTaskLoader<Movies> {

        final TMDB mTmdb;
        Movies mMovies;

        MoviesLoader(Context context, TMDB tmdb) {
            super(context);
            mTmdb = tmdb;
        }

        @Override
        protected void onStartLoading() {
            if (mMovies == null) {
                forceLoad();
            } else {
                deliverResult(mMovies);
            }
        }

        @Override
        public Movies loadInBackground() {
            try {
                return mTmdb.movies();
            } catch (IOException e) {
                Log.e(TAG, "Failure on get movies", e);
                return null;
            }
        }

        @Override
        public void deliverResult(Movies data) {
            mMovies = data;
            super.deliverResult(data);
        }
    }

}
