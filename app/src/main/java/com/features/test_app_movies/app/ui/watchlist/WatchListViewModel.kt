package com.features.test_app_movies.app.ui.watchlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.app.common.default
import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.db.models.DBMovies
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class WatchListViewModel(private val dbRepository: DBRepository) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    val watchlistLiveData = MutableLiveData<List<DBMovies>>()
    val state = MutableLiveData<WatchListState>().default(WatchListState.DefaultState())

    fun loadData() {
        state.postValue(WatchListState.LoadingState())
        compositeDisposable.add(
            dbRepository.getAllMovies()
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
            dbRepository.removeMovie(dbObject)
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

class WatchlistViewModelFactory @Inject constructor(
    private val dbRepository: DBRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WatchListViewModel(dbRepository) as T
    }
}