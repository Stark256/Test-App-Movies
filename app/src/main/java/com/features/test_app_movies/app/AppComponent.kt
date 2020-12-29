package com.features.test_app_movies.app

import android.app.Application
import com.features.test_app_movies.app.ui.details.DetailsActivity
import com.features.test_app_movies.app.ui.main.MainActivity
import com.features.test_app_movies.app.ui.watchlist.WatchlistActivity
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
    AppModule::class
))

interface AppComponent {

    fun application() : Application
//    fun apiService() : ApiService
//    fun db() : AppDatabase

    fun inject(activity: MainActivity)
    fun inject(activityMovie: DetailsActivity)
    fun inject(activity: WatchlistActivity)

    fun inject(app: Application)

}