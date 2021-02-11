package com.features.test_app_movies.api.models.tv_models

import android.os.Parcelable
import com.features.test_app_movies.api.models.Keyword
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVKeywordsList(
    @SerializedName("id") var id: Long?,
    @SerializedName("results") var keywords: ArrayList<Keyword>?
) : Parcelable