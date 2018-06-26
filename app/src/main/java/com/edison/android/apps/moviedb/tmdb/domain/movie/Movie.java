package com.edison.android.apps.moviedb.tmdb.domain.movie;

import android.os.Parcelable;

import java.util.Date;

public interface Movie extends Parcelable {

    Parcelable.Creator<Movie> CREATOR = new MovieModel.Creator();

    /**
     * Columns
     */
    String ID = "id";

    String ORIGINAL_TITLE = "original_title";

    String POSTER_PATH = "poster_path";

    String OVERVIEW = "overview";

    String RELEASE_DATE = "release_date";

    String VOTE_AVERAGE = "vote_average";

    String POPULARITY = "popularity";

    String FAVORITE = "favorite";

    /**
     * @return id
     */
    String id();

    /**
     * @return Título original
     */
    String originalTitle();

    /**
     * @return Caminho do poster
     */
    String posterPath();

    /**
     * @return Sinopse
     */
    String overview();

    /**
     * @return Data de lançamento
     */
    Date releaseDate();

    /**
     * @return Avaliação dos usuários
     */
    double voteAverage();

    /**
     * @return Popularidade
     */
    double popularity();

    /**
     * @return Favorite
     */
    boolean favorite();

}
