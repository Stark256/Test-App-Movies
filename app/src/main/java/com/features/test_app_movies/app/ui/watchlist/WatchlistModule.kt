package com.features.test_app_movies.app.ui.watchlist

import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class WatchlistModule {

    @Provides
    fun provideWatchlistViewModelFactory(dbRepository: DBRepository) : WatchlistViewModelFactory {
        return WatchlistViewModelFactory(dbRepository)
    }


}