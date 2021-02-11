package com.features.test_app_movies.db

import android.content.Context
import androidx.room.Room
import com.features.test_app_movies.db.dao.MoviesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule(private val context: Context, private val dbName: String) {

    @Singleton
    @Provides
    fun provideDatabase() : AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                dbName
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideMoviesDao(db: AppDatabase) : MoviesDao {
        return db.getMoviesDao()
    }


}