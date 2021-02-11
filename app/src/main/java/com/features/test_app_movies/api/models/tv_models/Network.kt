package com.features.test_app_movies.api.models.tv_models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Network (
    @SerializedName("id") var id: Long,
    @SerializedName("name") var name: String?,
    @SerializedName("logo_path") var logoPath: String?,
    @SerializedName("origin_country") var originalCountry: String?
) : Parcelable

/*
* "name": "HBO",
        "id": 49,
        "logo_path": "/tuomPhY2UtuPTqqFnKMVHvSb724.png",
        "origin_country": "US"
* */