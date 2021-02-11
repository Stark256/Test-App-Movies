package com.features.test_app_movies.app

import android.app.Application
import com.features.test_app_movies.api.ApiModule
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.ui.details.DetailsActivity
import com.features.test_app_movies.app.ui.details.DetailsViewModelModule
import com.features.test_app_movies.app.ui.main.MainActivity
import com.features.test_app_movies.app.ui.main.MainViewModelModule
import com.features.test_app_movies.app.ui.watchlist.WatchlistActivity
import com.features.test_app_movies.app.ui.watchlist.WatchlistViewModelModule
import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.DBModule
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
    AppModule::class,
    ApiModule::class,
    DBModule::class,
    MainViewModelModule::class,
    DetailsViewModelModule::class,
    WatchlistViewModelModule::class
))

interface AppComponent {

    fun application() : Application
    fun apiService() : ApiService
    fun db() : AppDatabase

    fun inject(activity: MainActivity)
    fun inject(activityMovie: DetailsActivity)
    fun inject(activity: WatchlistActivity)

    fun inject(app: Application)

}