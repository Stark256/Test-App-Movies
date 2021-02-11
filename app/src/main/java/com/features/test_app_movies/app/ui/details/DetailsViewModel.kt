package com.features.test_app_movies.app.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.app.common.default
import com.features.test_app_movies.app.common.keywordsToString
import com.features.test_app_movies.db.AppDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsViewModel(private val api: ApiService, private val db: AppDatabase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val state = MutableLiveData<DetailsState>().default(initialValue = DetailsState.DefaultState())
    val data = MutableLiveData<BaseDetails>()


    fun loadDetails(id: Long?, type: BaseDetails.ShowType?) {
        state.postValue(DetailsState.LoadingState())
        if(id != null && type != null)
            when(type) {
                BaseDetails.ShowType.TYPE_MOVIE -> loadMovieDetails(id)
                BaseDetails.ShowType.TYPE_TV -> loadTVDetails(id)
            }
        else state.postValue(DetailsState.ErrorState("Error occured"))
    }

    private fun loadMovieDetails(id: Long) {
        compositeDisposable.add(api.getMovieDetails(id = id)
            .zipWith(api.getMovieKeywords(id), { t1, t2 ->
                t1.apply { t2.keywords?.let { keywords = it.keywordsToString() }}
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                state.postValue(DetailsState.Success())
                data.postValue(it)
            }, {
                state.postValue(DetailsState.ErrorState(it.localizedMessage ?: "Error occured"))
            }))
    }

    private fun loadTVDetails(id: Long) {
        compositeDisposable.add(api.getTVDetails(id = id)
            .zipWith(api.getTVKeywords(id), { t1, t2 ->
                t1.apply { t2.keywords?.let { keywords = it.keywordsToString() } }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                state.postValue(DetailsState.Success())
                data.postValue(it)
            }, {
                state.postValue(DetailsState.ErrorState(it.localizedMessage ?: "Error occured"))
            }))
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
