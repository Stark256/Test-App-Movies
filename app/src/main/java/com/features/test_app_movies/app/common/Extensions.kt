package com.features.test_app_movies.app.common

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.api.models.Genre
import com.features.test_app_movies.api.models.Keyword
import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.app.ui.details.DetailActivityDelegate
import com.features.test_app_movies.db.models.DBMovies
import java.io.ByteArrayOutputStream
import java.io.Serializable
import kotlin.properties.ReadWriteProperty

// Safe Let

fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, T6: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, p6: T6?, block: (T1, T2, T3, T4, T5, T6)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null && p6 != null) block(p1, p2, p3, p4, p5, p6) else null
}


fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { value = initialValue }
fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }

fun ArrayList<Keyword>.keywordsToString() : String {
    var result = ""
    for(i in 0 until size) {
        result += if(i != lastIndex) "${get(i).name}, " else get(i).name
    }
    return result
}

fun ArrayList<Genre>.genresToString() : String {
    var result = ""
    for(i in 0 until size) {
        result += if(i != lastIndex) "${get(i).genreName}, " else get(i).genreName
    }
    return result
}

// Glide
fun glideListener(success: (Bitmap?) -> Unit, error: (GlideException?) -> Unit)
= object : RequestListener<Bitmap> {
    override fun onLoadFailed(
        e: GlideException?, model: Any?,
        target: Target<Bitmap>?, isFirstResource: Boolean
    ): Boolean {
        error.invoke(e)
        return false
    }

    override fun onResourceReady(
        resource: Bitmap?, model: Any?,
        target: Target<Bitmap>?, dataSource: DataSource?,
        isFirstResource: Boolean): Boolean {
        success.invoke(resource)
        return false
    }
}

fun Bitmap.bitmapToByteArr() : ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun ByteArray.byteArrToBitmap() : Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

fun toShowType(value: String?) : BaseDetails.ShowType {
    return when(value) {
        BaseDetails.ShowType.TYPE_MOVIE.value -> BaseDetails.ShowType.TYPE_MOVIE
        BaseDetails.ShowType.TYPE_TV.value -> BaseDetails.ShowType.TYPE_TV
        else -> BaseDetails.ShowType.TYPE_MOVIE
    }
}

fun MovieDetails.toDBMovies() : DBMovies {
    return DBMovies(
            movieID = id,
            type = type.value,
            title = title,
            originalTitle = originalTitle,
            posterPath = posterPath,
            backPosterPath = backPath,
            posterBytes = posterBytes,
            backPosterBytes = posterBackBytes,
            budget = budget,
            revenue = revenue,
            status = status,
            releaseDate = releaseDate,
            popularity = popularity,
            voteAverage = voteAverage,
            runtime = runtime,
            tagline = tagline,
            overview = overview,
            keywords = keywords,
            genres = genres?.genresToString() ?: "-",

    )
}

fun TVDetails.toDBMovies() : DBMovies {
    return DBMovies(
            movieID = id,
            type = type.value,
            title = title,
            originalTitle = "",
            posterPath = posterPath,
            backPosterPath = backPath,
            posterBytes = posterBytes,
            backPosterBytes = posterBackBytes,
            budget = 0,
            revenue = 0,
            status = status,
            releaseDate = firstAirDate,
            popularity = popularity,
            voteAverage = voteAverage,
            runtime = episodeRunTime?.firstOrNull() ?: 0,
            tagline = tagline,
            overview = overview,
            keywords = keywords,
            genres = genres?.genresToString() ?: "-",

            )
}

// Delegates
fun <T> Intent.putExtra(key: String, value: T) {
    when(value) {
        is Boolean -> putExtra(key, value)
        is Long -> putExtra(key, value)
        is String -> putExtra(key, value)
        is Int -> putExtra(key, value)
        is Float -> putExtra(key, value)
        is Serializable -> putExtra(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T: Any> extras() : ReadWriteProperty<Activity, T> = DetailActivityDelegate()

