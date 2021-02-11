package com.features.test_app_movies.api.models

import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.tv_models.TVDetails

abstract class BaseShows {

}

data class TopRatedShows(
    val movies: ArrayList<MovieDetails>,
    val tvs: ArrayList<TVDetails>
) : BaseShows()

data class PopularShows(
    val movies: ArrayList<MovieDetails>,
    val tvs: ArrayList<TVDetails>
) : BaseShows()

data class TodayShows(
    val movies: ArrayList<MovieDetails>,
    val tvs: ArrayList<TVDetails>
) : BaseShows()