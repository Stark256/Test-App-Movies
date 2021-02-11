package com.features.test_app_movies.app.ui.details

sealed class DetailsState {

    class ErrorState(val message: String?) : DetailsState()
    class LoadingState() : DetailsState()
    class Success() : DetailsState()
    class DefaultState() : DetailsState()

}
