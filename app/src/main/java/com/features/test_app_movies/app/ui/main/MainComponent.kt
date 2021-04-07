package com.features.test_app_movies.app.ui.main

import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : MainComponent
    }

    fun inject(mainActivity: MainActivity)
}