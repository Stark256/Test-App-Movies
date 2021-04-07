package com.features.test_app_movies.app.ui.details

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.app.repositories.TVsRepository
import com.features.test_app_movies.app.ui.main.MainViewModelFactory
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    fun provideDetailsViewModelFactory(moviesRepository: MoviesRepository, tVsRepository: TVsRepository) : DetailsViewModelFactory {
        return DetailsViewModelFactory(moviesRepository, tVsRepository)
    }

}