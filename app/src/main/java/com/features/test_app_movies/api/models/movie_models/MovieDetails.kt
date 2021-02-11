package com.features.test_app_movies.api.models.movie_models

import android.os.Parcelable
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.api.models.Genre
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetails(
    @SerializedName("id") var id: Long,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("backdrop_path") var backPath: String?,
    @SerializedName("budget") var budget: Long?,
    @SerializedName("revenue") var revenue: Long?,
    @SerializedName("title") var title: String?,
    @SerializedName("original_title") var originalTitle: String?,
    @SerializedName("status") var status: String?,
    @SerializedName("release_date") var releaseDate: String?,
    @SerializedName("popularity") var popularity: Float?,
    @SerializedName("vote_average") var voteAverage: Float?,
    @SerializedName("runtime") var runtime: Int?,
    @SerializedName("tagline") var tagline: String?,
    @SerializedName("overview") var overview: String?,
    @SerializedName("genres") var genres: ArrayList<Genre>?

    // Not using properties below

//    @SerializedName("genre_ids") var genreIds: Any?,
//    @SerializedName("adult") var adult: Any?,
//    @SerializedName("belongs_to_collection") var belongToCollection: Any?,
//    @SerializedName("homepage") var homepage: Any?,
//    @SerializedName("imdb_id") var imdbId: Any?,
//    @SerializedName("original_language") var originalLanguage: Any?,
//    @SerializedName("production_companies") var productionCompanies: Any?,
//    @SerializedName("production_countries") var productionCountries: Any?,
//    @SerializedName("spoken_languages") var spokenLanguages: Any?,
//    @SerializedName("video") var video: Any?,
//    @SerializedName("vote_count") var voteCount: Any?
) : Parcelable, BaseDetails() {
    override val type: ShowType
        get() = ShowType.TYPE_MOVIE

    @IgnoredOnParcel
    var keywords: String = "-"

    @IgnoredOnParcel
    var posterBytes: ByteArray? = null
    @IgnoredOnParcel
    var posterBackBytes: ByteArray? = null
}



/*{
    "id": 550,
    "poster_path": null,
    "backdrop_path": "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
    "budget": 63000000,
    "revenue": 100853753,
    "original_title": "Fight Club",
    "title": "Fight Club",
    "status": "Released",
    "release_date": "1999-10-12",
    "popularity": 0.5,
    "runtime": 139,
    "tagline": "How much can you know about yourself if you've never been in a fight?",
    "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
     "genres": [
    {
        "id": 18,
        "name": "Drama"
    }
    ],



    "adult": false,
    "belongs_to_collection": null,
    "homepage": "",
    "imdb_id": "tt0137523",
    "original_language": "en",
    "production_companies": [
    {
        "id": 508,
        "logo_path": "/7PzJdsLGlR7oW4J0J5Xcd0pHGRg.png",
        "name": "Regency Enterprises",
        "origin_country": "US"
    },
    {
        "id": 711,
        "logo_path": null,
        "name": "Fox 2000 Pictures",
        "origin_country": ""
    },
    {
        "id": 20555,
        "logo_path": null,
        "name": "Taurus Film",
        "origin_country": ""
    },
    {
        "id": 54050,
        "logo_path": null,
        "name": "Linson Films",
        "origin_country": ""
    },
    {
        "id": 54051,
        "logo_path": null,
        "name": "Atman Entertainment",
        "origin_country": ""
    },
    {
        "id": 54052,
        "logo_path": null,
        "name": "Knickerbocker Films",
        "origin_country": ""
    },
    {
        "id": 25,
        "logo_path": "/qZCc1lty5FzX30aOCVRBLzaVmcp.png",
        "name": "20th Century Fox",
        "origin_country": "US"
    }
    ],
    "production_countries": [
    {
        "iso_3166_1": "US",
        "name": "United States of America"
    }
    ],
    "spoken_languages": [
    {
        "iso_639_1": "en",
        "name": "English"
    }
    ],
    "video": false,
    "vote_average": 7.8,
    "vote_count": 3439
}*/