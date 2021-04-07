package com.features.test_app_movies.app.repositories

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.api.models.tv_models.TVKeywordsList
import com.features.test_app_movies.api.models.tv_models.TVsList
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TVsRepository @Inject constructor(private val api : ApiService) : TVSRepositoryInterface {
    override suspend fun loadTopRatedTVs(): TVsList = api.getTopRatedTVs()
    override suspend fun loadPopularTVs(): TVsList = api.getPopularTVs()
    override suspend fun loadTodayTVs(): TVsList = api.getTodayTVs()

    override fun loadTVDetails(id: Long): Single<TVDetails> = api.getTVDetails(id = id)
    override fun loadTVsKeywords(id: Long): Single<TVKeywordsList> = api.getTVKeywords(id = id)
}


interface TVSRepositoryInterface {

    suspend fun loadTopRatedTVs() : TVsList
    suspend fun loadPopularTVs() : TVsList
    suspend fun loadTodayTVs() : TVsList

    fun loadTVDetails(id: Long) : Single<TVDetails>
    fun loadTVsKeywords(id: Long) : Single<TVKeywordsList>
}