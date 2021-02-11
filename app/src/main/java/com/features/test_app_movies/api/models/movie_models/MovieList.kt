package com.features.test_app_movies.api.models.movie_models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MovieList(
    @SerializedName("results") var results: ArrayList<MovieDetails>?,
    @SerializedName("total_results") var totalResults: Int?,
//    @SerializedName("total_pages") var totalPages: Any?,
//    @SerializedName("page") var page: Any?
) : Parcelable