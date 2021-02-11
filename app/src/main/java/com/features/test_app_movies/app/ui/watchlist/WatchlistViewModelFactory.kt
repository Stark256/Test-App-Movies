package com.features.test_app_movies.app.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.db.AppDatabase
import javax.inject.Inject

class WatchlistViewModelFactory @Inject constructor(private val db: AppDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WatchListViewModel(db) as T
    }
}