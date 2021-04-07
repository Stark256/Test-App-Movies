package com.features.test_app_movies.app.repositories

import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.models.DBMovies
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DBRepository @Inject constructor(private val db : AppDatabase) : DBRepositoryInterface {
    override fun saveToWatchlist(dbMovies: DBMovies): Completable = RxJavaBridge.toV3Completable(db.getMoviesDao().insert(dbMovies))
    override fun getMovieByID(id: Long): Single<List<DBMovies>> = RxJavaBridge.toV3Single(db.getMoviesDao().getMovieByID(id))

    override fun getAllMovies(): Single<List<DBMovies>> = RxJavaBridge.toV3Single(db.getMoviesDao().getAllMovies())
    override fun removeMovie(movie: DBMovies): Completable = RxJavaBridge.toV3Completable(db.getMoviesDao().delete(movie))
}


interface DBRepositoryInterface {
    fun saveToWatchlist(dbMovies: DBMovies) : Completable
    fun getMovieByID(id: Long) : Single<List<DBMovies>>

    fun getAllMovies() : Single<List<DBMovies>>
    fun removeMovie(movie: DBMovies) : Completable
}