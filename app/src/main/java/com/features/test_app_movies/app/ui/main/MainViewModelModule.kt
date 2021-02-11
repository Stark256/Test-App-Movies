package com.features.test_app_movies.app.ui.main

import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class MainViewModelModule {

    @Provides
    fun provideMainViewModelFactory(api: ApiService, appDatabase: AppDatabase) : MainViewModelFactory {
        return MainViewModelFactory(api, appDatabase)
    }

}