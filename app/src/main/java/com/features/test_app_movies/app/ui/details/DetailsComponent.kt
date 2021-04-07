package com.features.test_app_movies.app.ui.details

import dagger.Subcomponent

@Subcomponent(modules = [DetailsModule::class])
interface DetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : DetailsComponent
    }

    fun inject(detailsActivity: DetailsActivity)
}