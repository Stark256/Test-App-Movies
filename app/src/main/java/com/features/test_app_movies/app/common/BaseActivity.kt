package com.features.test_app_movies.app.common

import androidx.appcompat.app.AppCompatActivity
import com.features.test_app_movies.app.App
import com.features.test_app_movies.app.AppComponent

open class BaseActivity: AppCompatActivity() {

    val appComponent: AppComponent
        get() { return (application as App).appComponent }

}