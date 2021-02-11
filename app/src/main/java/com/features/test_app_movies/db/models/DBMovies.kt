package com.features.test_app_movies.db.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream

@Entity(tableName = "movies_table")
class DBMovies(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "mID") var movieID: Long?,
        @ColumnInfo(name = "mType") var type: String?,
        @ColumnInfo(name = "mTitle") var title: String?,
        @ColumnInfo(name = "mOriginalTitle") var originalTitle: String?,
        @ColumnInfo(name = "mPosterBytes") var posterBytes: ByteArray?,
        @ColumnInfo(name = "mBackPosterBytes") var backPosterBytes: ByteArray?,
        @ColumnInfo(name = "mPosterPath") var posterPath: String?,
        @ColumnInfo(name = "mBackPosterPath") var backPosterPath: String?,
        @ColumnInfo(name = "mBudget") var budget: Long?,
        @ColumnInfo(name = "mRevenue") var revenue: Long?,
        @ColumnInfo(name = "mStatus") var status: String?,
        @ColumnInfo(name = "mReleaseDate") var releaseDate: String?,
        @ColumnInfo(name = "mPopularity") var popularity: Float?,
        @ColumnInfo(name = "mVoteAverage") var voteAverage: Float?,
        @ColumnInfo(name = "mRuntime") var runtime: Int?,
        @ColumnInfo(name = "mTagline") var tagline: String?,
        @ColumnInfo(name = "mOverview") var overview: String?,
        @ColumnInfo(name = "mGenres") var genres : String?,
        @ColumnInfo(name = "mKeywords") var keywords : String?
 )