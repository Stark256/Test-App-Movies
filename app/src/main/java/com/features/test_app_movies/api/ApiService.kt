package com.features.test_app_movies.api

import com.features.test_app_movies.api.models.tv_models.TVKeywordsList
import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.movie_models.MovieKeywordsList
import com.features.test_app_movies.api.models.movie_models.MovieList
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.api.models.tv_models.TVsList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    // Popular

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : MovieList

    @GET("/3/tv/popular")
    suspend fun getPopularTVs(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : TVsList


    // Top Rated

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : MovieList

    @GET("/3/tv/top_rated")
    suspend fun getTopRatedTVs(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : TVsList


    // Now Playing

    @GET("/3/movie/now_playing")
    suspend fun getTodayMovies(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : MovieList

    @GET("/3/tv/airing_today")
    suspend fun getTodayTVs(
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
    ) : TVsList


    // Details

    @GET("/3/movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Long,
        @Query("language") language: String = "en-US"
    ) : Single<MovieDetails>

    @GET("/3/tv/{tv_id}")
    fun getTVDetails(
        @Path("tv_id") id: Long,
        @Query("language") language: String = "en-US"
    ) : Single<TVDetails>


    // Keywords

    @GET("/3/movie/{movie_id}/keywords")
    fun getMovieKeywords(
            @Path("movie_id") id: Long
    ) : Single<MovieKeywordsList>

    @GET("/3/tv/{tv_id}/keywords")
    fun getTVKeywords(
            @Path("tv_id") id: Long
    ) : Single<TVKeywordsList>


}