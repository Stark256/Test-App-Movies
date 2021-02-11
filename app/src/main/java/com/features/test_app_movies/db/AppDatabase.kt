package com.features.test_app_movies.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.features.test_app_movies.db.dao.MoviesDao
import com.features.test_app_movies.db.models.DBMovies

@Database(entities = arrayOf(DBMovies::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {



    abstract fun getMoviesDao() : MoviesDao
}