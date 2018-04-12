package com.edison.udacity.android.moviedb.tmdb;

import android.net.Uri;
import android.support.annotation.StringDef;

import com.edison.udacity.android.moviedb.BuildConfig;
import com.edison.udacity.android.moviedb.api.media.JsonMediaArray;
import com.edison.udacity.android.moviedb.model.MovieCreator;
import com.edison.udacity.android.moviedb.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TMDB {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String PARAM_API_KEY = "api_key";

    public static final String POPULAR_URL = "popular";
    public static final String TOP_RATED_URL = "top_rated";

    @StringDef({POPULAR_URL, TOP_RATED_URL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UrlPath {}

    public static TMDB popular() {
        return new TMDB(POPULAR_URL);
    }

    public static TMDB topRated() {
        return new TMDB(TOP_RATED_URL);
    }

    private Uri.Builder mUri;

    public TMDB(@UrlPath String url) {
        mUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(url)
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY);
    }

    public Movies movies() throws IOException {
        URL url = new URL(mUri.build().toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                if (in == null) {
                    throw new IOException("Internal error: " + connection.getResponseMessage());
                }
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    String response = scanner.next();
                    try {
                        JSONObject json = new JSONObject(response);
                        JSONArray results = json.getJSONArray("results");
                        return new Movies(new JsonMediaArray<>(results, new MovieCreator()));
                    } catch (JSONException e) {
                        throw new HttpRetryException("Malformed response: " + response, connection.getResponseCode());
                    }
                } else {
                    throw new HttpRetryException(connection.getResponseMessage(), connection.getResponseCode());
                }
            } else {
                throw new HttpRetryException(connection.getResponseMessage(), connection.getResponseCode());
            }
        } finally {
            connection.disconnect();
        }
    }



}
