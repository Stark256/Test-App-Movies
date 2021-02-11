package com.features.test_app_movies.app.ui.watchlist

sealed class WatchListState {
    class ErrorState(val message: String?): WatchListState()
    class LoadingState() : WatchListState()
    class DefaultState() : WatchListState()
    class Success() : WatchListState()
}
