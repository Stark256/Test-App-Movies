package com.features.test_app_movies.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.features.test_app_movies.db.models.DBMovies
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface MoviesDao : BaseDao<DBMovies> {

    @Query("SELECT * from movies_table")
    fun getAllMovies() : io.reactivex.Single<List<DBMovies>>

    @Query("SELECT * from movies_table where mID = :id")
    fun getMovieByID(id: Long) : io.reactivex.Single<List<DBMovies>>

}