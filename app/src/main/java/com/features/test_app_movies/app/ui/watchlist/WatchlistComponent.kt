package com.features.test_app_movies.app.ui.watchlist

import dagger.Subcomponent

@Subcomponent(modules = [WatchlistModule::class])
interface WatchlistComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : WatchlistComponent
    }

    fun inject(watchlistActivity: WatchlistActivity)

}