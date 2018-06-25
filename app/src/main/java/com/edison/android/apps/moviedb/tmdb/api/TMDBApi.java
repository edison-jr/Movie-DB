package com.edison.android.apps.moviedb.tmdb.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.content.Loader;
import android.util.SparseArray;

import com.edison.android.apps.moviedb.BuildConfig;
import com.edison.android.apps.moviedb.tmdb.TMDB;
import com.edison.android.apps.moviedb.tmdb.db.TMDBDatabase;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MovieModel;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MovieReader;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.edison.android.apps.moviedb.tmdb.domain.movie.MoviesLoader;
import com.edison.android.apps.moviedb.tmdb.domain.review.Review;
import com.edison.android.apps.moviedb.tmdb.domain.review.ReviewReader;
import com.edison.android.apps.moviedb.tmdb.domain.review.Reviews;
import com.edison.android.apps.moviedb.tmdb.domain.review.ReviewsLoader;
import com.edison.android.apps.moviedb.tmdb.domain.video.Video;
import com.edison.android.apps.moviedb.tmdb.domain.video.VideoReader;
import com.edison.android.apps.moviedb.tmdb.domain.video.Videos;
import com.edison.android.apps.moviedb.tmdb.domain.video.VideosLoader;
import com.edison.android.tools.io.JsonResponse;
import com.edison.android.tools.media.MediaArray;
import com.edison.android.tools.media.MediaCreator;
import com.edison.android.tools.media.json.JsonMediaArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpRetryException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TMDBApi implements TMDB {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String PARAM_API_KEY = "api_key";

    private static final String POPULAR_URL = "popular";
    private static final String TOP_RATED_URL = "top_rated";
    private static final String VIDEOS_URL = "videos";
    private static final String REVIEWS_URL = "reviews";

    @StringDef({POPULAR_URL, TOP_RATED_URL, VIDEOS_URL, REVIEWS_URL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UrlPath {}

    public static Loader<Movies> popular(@NonNull Context context) {
        return new MoviesLoader(context, new TMDBApi(context).path(POPULAR_URL));
    }

    public static Loader<Movies> topRated(@NonNull Context context) {
        return new MoviesLoader(context, new TMDBApi(context).path(TOP_RATED_URL));
    }

    public static Loader<Videos> videos(@NonNull Context context, String id) {
        return new VideosLoader(context, new TMDBApi(context).path(id, VIDEOS_URL));
    }

    public static Loader<Reviews> reviws(@NonNull Context context, String id) {
        return new ReviewsLoader(context, new TMDBApi(context).path(id, REVIEWS_URL));
    }

    private final Context mContext;
    private final OkHttpClient mHttpClient;
    private final HttpUrl.Builder mUrl;

    private TMDBApi(@NonNull Context context) {
        mContext = context;
        mHttpClient = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(BASE_URL);
        assert url != null;
        mUrl = url.newBuilder()
                .addQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY);

    }

    public TMDBApi path(@UrlPath String path) {
        mUrl.addPathSegment(path);
        return this;
    }

    public TMDBApi path(String id, @UrlPath String path) {
        mUrl.addPathSegment(id).addPathSegment(path);
        return this;
    }

    private Call call() {
        Request request = new Request.Builder()
                .url(mUrl.build())
                .build();
        return mHttpClient.newCall(request);
    }

    private <T> MediaArray<T> request(MediaCreator<T> creator) throws IOException {
        Response response = call().execute();
        ResponseResults<T> responseResults = new ResponseResults<>(response, creator);
        return responseResults.results();
    }

    public Movies movies() throws IOException {
        MediaArray<Movie> movies = request(new MovieReader.Creator());
        SparseArray<String> favoties = new TMDBDatabase(mContext).favoritesId();
        return new MoviesApi(movies, favoties);
    }

    @Override
    public Videos videos() throws IOException {
        MediaArray<Video> videos = request(new VideoReader.Creator());
        return new Videos(videos);
    }

    @Override
    public Reviews reviews() throws IOException {
        MediaArray<Review> reviews = request(new ReviewReader.Creator());
        return new Reviews(reviews);
    }

    private static class ResponseResults<T> {

        final MediaArray<T> mResults;

        ResponseResults(Response response, MediaCreator<T> creator) throws IOException {
            JsonResponse jsonResponse = new JsonResponse(response);
            try {
                JSONArray results = jsonResponse.json().getJSONArray("results");
                mResults = new JsonMediaArray<>(results, creator);
            } catch (JSONException e) {
                throw new HttpRetryException("Malformed results: " + response, response.code());
            }
        }

        MediaArray<T> results() {
            return mResults;
        }

    }

    public static class MoviesApi extends Movies {

        final SparseArray<String> mFavorites;

        public MoviesApi(@NonNull MediaArray<Movie> movies, SparseArray<String> favorites) {
            super(movies);
            mFavorites = favorites;
        }

        public MoviesApi(@NonNull Movies movies, SparseArray<String> favorites) {
            super(movies);
            mFavorites = favorites;
        }

        @Override
        public Movie get(int position) {
            Movie movie = super.get(position);
            if (mFavorites.indexOfKey(movie.id().hashCode()) > -1) {
                return new MovieModel(movie, true);
            } else {
                return movie;
            }
        }

    }

}
