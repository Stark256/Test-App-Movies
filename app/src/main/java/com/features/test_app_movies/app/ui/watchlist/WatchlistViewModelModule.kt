package com.features.test_app_movies.app.ui.watchlist

import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class WatchlistViewModelModule {

    @Provides
    fun provideWatchlistViewModelFactory(appDatabase: AppDatabase) : WatchlistViewModelFactory {
        return WatchlistViewModelFactory(appDatabase)
    }


}