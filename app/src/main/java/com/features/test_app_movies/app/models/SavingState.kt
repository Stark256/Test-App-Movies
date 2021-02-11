package com.features.test_app_movies.app.models

sealed class SavingState {
    class Saving() : SavingState()
    class AlreadyExist(val title: String?) : SavingState()
    class Error(val message: String?) : SavingState()
    class Saved(val title: String?) : SavingState()
}
