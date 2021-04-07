package com.features.test_app_movies.app.ui.main

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.app.repositories.TVsRepository
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun provideMainViewModelFactory(moviesRepository: MoviesRepository, tVsRepository: TVsRepository, dbRepository: DBRepository) : MainViewModelFactory {
        return MainViewModelFactory(moviesRepository, tVsRepository, dbRepository)
    }

}