package com.features.test_app_movies.app.ui.watchlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.features.test_app_movies.app.common.default
import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.models.DBMovies
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class WatchListViewModel(private val db: AppDatabase) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    val watchlistLiveData = MutableLiveData<List<DBMovies>>()
    val state = MutableLiveData<WatchListState>().default(WatchListState.DefaultState())

    fun loadData() {
        state.postValue(WatchListState.LoadingState())
        compositeDisposable.add(
            RxJavaBridge.toV3Single(db.getMoviesDao().getAllMovies())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   state.postValue(WatchListState.Success())
                    watchlistLiveData.postValue(it)
                }, {
                    state.postValue(WatchListState.ErrorState(it.localizedMessage ?: "Error occured"))
                }))
    }

    fun removeFromWatchlist(dbObject: DBMovies) {
        compositeDisposable.add(
            RxJavaBridge.toV3Completable(db.getMoviesDao().delete(dbObject))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadData()
                }, {
                    state.postValue(WatchListState.ErrorState(it.localizedMessage ?: "Error occured"))
                }))
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}