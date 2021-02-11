package com.features.test_app_movies.api.models.tv_models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class TVsList(
    @SerializedName("results") var results: ArrayList<TVDetails>?,
    @SerializedName("total_results") var totalResults: Int?,
//    @SerializedName("total_pages") var totalPages: Any?,
//    @SerializedName("page") var page: Any?
) : Parcelable