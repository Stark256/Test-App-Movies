package com.features.test_app_movies.api.models

abstract class BaseDetails {

    enum class ShowType(val value: String) {
        TYPE_TV("TV"),
        TYPE_MOVIE("Movie")
    }

    abstract val type: ShowType

}