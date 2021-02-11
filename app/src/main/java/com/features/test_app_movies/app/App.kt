package com.features.test_app_movies.app

import android.app.Application
import com.features.test_app_movies.R
import com.features.test_app_movies.api.ApiModule
import com.features.test_app_movies.db.DBModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule(getString(R.string.base_url), getString(R.string.api_key)))
            .dBModule(DBModule(this, getString(R.string.db_name)))
            .build()

        appComponent.inject(this)
    }
}