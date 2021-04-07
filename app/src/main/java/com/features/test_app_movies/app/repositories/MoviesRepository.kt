package com.features.test_app_movies.app.repositories

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.movie_models.MovieKeywordsList
import com.features.test_app_movies.api.models.movie_models.MovieList
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.api.models.tv_models.TVKeywordsList
import com.features.test_app_movies.api.models.tv_models.TVsList
import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.models.DBMovies
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val api: ApiService) : MoviesRepositoryInterface {
    override suspend fun loadTopRatedMovies(): MovieList = api.getTopRatedMovies()
    override suspend fun loadPopularMovies(): MovieList = api.getPopularMovies()
    override suspend fun loadTodayMovies(): MovieList = api.getTodayMovies()

    override fun loadMoviesDetails(id: Long): Single<MovieDetails> = api.getMovieDetails(id = id)
    override fun loadMoviesKeywords(id: Long): Single<MovieKeywordsList> = api.getMovieKeywords(id = id)
}

interface MoviesRepositoryInterface {
    suspend fun loadTopRatedMovies() : MovieList
    suspend fun loadPopularMovies() : MovieList
    suspend fun loadTodayMovies() : MovieList

    fun loadMoviesDetails(id: Long): Single<MovieDetails>
    fun loadMoviesKeywords(id: Long) : Single<MovieKeywordsList>
}