package com.features.test_app_movies.app

import android.app.Application
import com.features.test_app_movies.api.ApiModule
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.app.ui.details.DetailsComponent
import com.features.test_app_movies.app.ui.details.DetailsModule
import com.features.test_app_movies.app.ui.main.MainComponent
import com.features.test_app_movies.app.ui.main.MainModule
import com.features.test_app_movies.app.ui.watchlist.WatchlistComponent
import com.features.test_app_movies.app.ui.watchlist.WatchlistModule
import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.DBModule
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
    AppModule::class,
    ApiModule::class,
    DBModule::class
//    ,
//    MainModule::class,
//    DetailsModule::class,
//    WatchlistModule::class
))

interface AppComponent {

    fun mainComponent() : MainComponent.Factory
    fun detailsComponent() : DetailsComponent.Factory
    fun watchlistComponent() : WatchlistComponent.Factory

    fun application() : Application
    fun apiService() : ApiService
    fun db() : AppDatabase

    fun inject(app: Application)

}