package com.features.test_app_movies.db.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): io.reactivex.Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T) : io.reactivex.Completable

    @Update
    fun update(obj: T) : io.reactivex.Completable

    @Delete
    fun delete(obj: T) : io.reactivex.Completable
}