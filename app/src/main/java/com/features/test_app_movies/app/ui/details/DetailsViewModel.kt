package com.features.test_app_movies.app.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.app.common.default
import com.features.test_app_movies.app.common.keywordsToString
import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.app.repositories.TVsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel(
    private val moviesRepository: MoviesRepository,
    private val tVsRepository: TVsRepository
) : ViewModel() {

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
        compositeDisposable.add(moviesRepository.loadMoviesDetails(id = id)
            .zipWith(moviesRepository.loadMoviesKeywords(id), { t1, t2 ->
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
        compositeDisposable.add(tVsRepository.loadTVDetails(id = id)
            .zipWith(tVsRepository.loadTVsKeywords(id), { t1, t2 ->
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


class DetailsViewModelFactory @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val tVsRepository: TVsRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(moviesRepository, tVsRepository) as T
    }
}