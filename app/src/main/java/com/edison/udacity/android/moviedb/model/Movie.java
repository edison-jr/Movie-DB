package com.edison.udacity.android.moviedb.model;

import java.util.Date;

public interface Movie {

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

}
