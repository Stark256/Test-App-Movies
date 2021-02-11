package com.features.test_app_movies.app.ui.details

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.ui.main.MainViewModelFactory
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DetailsViewModelModule {

    @Provides
    fun provideDetailsViewModelFactory(api: ApiService, db: AppDatabase) : DetailsViewModelFactory {
        return DetailsViewModelFactory(api, db)
    }

}