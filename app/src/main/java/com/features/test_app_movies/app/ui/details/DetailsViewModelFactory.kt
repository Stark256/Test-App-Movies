package com.features.test_app_movies.app.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.db.AppDatabase
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(private val api: ApiService, private val db: AppDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(api, db) as T
    }
}