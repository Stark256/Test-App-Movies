package com.features.test_app_movies.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.db.AppDatabase
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val api: ApiService, private val db: AppDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(api, db) as T
    }
}