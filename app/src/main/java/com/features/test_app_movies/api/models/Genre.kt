package com.features.test_app_movies.api.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Genre(
    @SerializedName("id") var id: Long,
    @SerializedName("name") var genreName: String
) : Parcelable