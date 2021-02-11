package com.features.test_app_movies.api.models.movie_models

import android.os.Parcelable
import com.features.test_app_movies.api.models.Keyword
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieKeywordsList(
    @SerializedName("id") var id: Long?,
    @SerializedName("keywords") var keywords: ArrayList<Keyword>?
) : Parcelable