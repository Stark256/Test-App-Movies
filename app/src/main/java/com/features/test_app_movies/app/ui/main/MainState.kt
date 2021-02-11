package com.features.test_app_movies.app.ui.main

sealed class MainState {
    class ErrorState(val message: String?): MainState()
    class LoadingState() : MainState()
    class DefaultState() : MainState()
    class Success() : MainState()
}
