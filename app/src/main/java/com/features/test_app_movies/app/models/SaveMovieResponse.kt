package com.features.test_app_movies.app.models

import com.features.test_app_movies.api.models.BaseDetails

data class SaveMovieResponse(
    val id: Long,
    val type: BaseDetails.ShowType,
    val poster: ByteArray?,
    val backPoster: ByteArray?)
