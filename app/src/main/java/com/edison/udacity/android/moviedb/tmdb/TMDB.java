package com.edison.udacity.android.moviedb.tmdb;

import android.support.annotation.StringDef;

import com.edison.udacity.android.moviedb.BuildConfig;
import com.edison.udacity.android.moviedb.api.media.JsonMediaArray;
import com.edison.udacity.android.moviedb.model.Movie;
import com.edison.udacity.android.moviedb.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpRetryException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TMDB {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String PARAM_API_KEY = "api_key";

    public static final String POPULAR_URL = "popular";
    public static final String TOP_RATED_URL = "top_rated";

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

    public Movies movies() throws IOException {
        Request request = new Request.Builder()
                .url(mUrl.build())
                .build();

        Response response = mHttpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String body = response.body().string();
            try {
                JSONObject json = new JSONObject(body);
                JSONArray results = json.getJSONArray("results");
                return new Movies(new JsonMediaArray<>(results, new Movie.Creator()));
            } catch (JSONException e) {
                throw new HttpRetryException("Malformed response: " + response, response.code());
            }
        } else {
            throw new  HttpRetryException(response.message(), response.code());
        }
    }



}
