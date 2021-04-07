package com.features.test_app_movies.app

import android.app.Application
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.app.repositories.TVsRepository
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication() : Application {
        return application
    }

    @Provides
    fun provideMoviesRepository(apiService: ApiService) : MoviesRepository = MoviesRepository(apiService)

    @Provides
    fun provideTVsRepository(apiService: ApiService) : TVsRepository = TVsRepository(apiService)

    @Provides
    fun provideDBRepository(db: AppDatabase) : DBRepository = DBRepository(db)
}