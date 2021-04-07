package com.features.test_app_movies.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.features.test_app_movies.api.models.*
import com.features.test_app_movies.app.common.*
import com.features.test_app_movies.app.customViews.SwitchButtonView
import com.features.test_app_movies.app.models.SaveMovieResponse
import com.features.test_app_movies.app.models.SavingState
import com.features.test_app_movies.app.repositories.DBRepository
import com.features.test_app_movies.app.repositories.MoviesRepository
import com.features.test_app_movies.app.repositories.TVsRepository
import com.features.test_app_movies.db.models.DBMovies
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val moviesRepository: MoviesRepository,
    private val tVsRepository: TVsRepository,
    private val dbRepository: DBRepository) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->  throw e}

    private val compositeDisposable = CompositeDisposable()
    val state = MutableLiveData<MainState>().default(initialValue = MainState.DefaultState())
    val savingState = MutableLiveData<SavingState>()

    val topRatedLiveData = MutableLiveData<ArrayList<BaseDetails>>()
    val popularLiveData = MutableLiveData<ArrayList<BaseDetails>>()
    val todayLiveData = MutableLiveData<ArrayList<BaseDetails>>()

    private var topRatedType : SwitchButtonView.SwitchType = SwitchButtonView.SwitchType.TYPE_TV
    private var popularType : SwitchButtonView.SwitchType = SwitchButtonView.SwitchType.TYPE_TV
    private var todayType : SwitchButtonView.SwitchType = SwitchButtonView.SwitchType.TYPE_TV

    private var topRatedShows: TopRatedShows? = null
    private var popularShows: PopularShows? = null
    private var todayShows: TodayShows? = null


    fun loadData() {
        launch(coroutineContext) {
            state.postValue(MainState.LoadingState())

            try{

                val topRated = async { getTopRatedShows() }.await()
                val popular = async { getPopularShows() }.await()
                val today = async { getTodayShows() }.await()

                topRatedShows = topRated
                popularShows = popular
                todayShows = today


                sendAllData()
                state.postValue(MainState.Success())

            } catch (e: Exception) {
                state.postValue(MainState.ErrorState(e.localizedMessage ?: "Error occured"))
            }

        }
    }

    fun changeTopRatedType(type: SwitchButtonView.SwitchType) {
        this.topRatedType = type
        sendTopRatedShows()
    }
    fun changePopularType(type: SwitchButtonView.SwitchType) {
        this.popularType = type
        sendPopularShows()
    }
    fun changeTodayType(type: SwitchButtonView.SwitchType) {
        this.todayType = type
        sendTodayShows()
    }


    private fun sendAllData() {
        sendTopRatedShows()
        sendPopularShows()
        sendTodayShows()
    }

    private fun sendTopRatedShows() {
        val arr = ArrayList<BaseDetails>()
        arr.addAll(if(topRatedType == SwitchButtonView.SwitchType.TYPE_TV)
            topRatedShows?.tvs ?: ArrayList()
        else
            topRatedShows?.movies ?: ArrayList())

        this.topRatedLiveData.postValue(arr)
    }
    private fun sendPopularShows() {
        val arr = ArrayList<BaseDetails>()
        arr.addAll(if(popularType == SwitchButtonView.SwitchType.TYPE_TV)
            popularShows?.tvs ?: ArrayList()
        else
            popularShows?.movies ?: ArrayList())
        this.popularLiveData.postValue(arr)
    }
    private fun sendTodayShows() {
        val arr = ArrayList<BaseDetails>()
        arr.addAll(if(todayType == SwitchButtonView.SwitchType.TYPE_TV)
            todayShows?.tvs ?: ArrayList()
        else
            todayShows?.movies ?: ArrayList())
        this.todayLiveData.postValue(arr)
    }


    private suspend fun getTopRatedShows() : TopRatedShows = withContext(Dispatchers.IO) {
        TopRatedShows(
            async { moviesRepository.loadTopRatedMovies() }.await().results ?: ArrayList(),
            async { tVsRepository.loadTopRatedTVs() }.await().results ?: ArrayList()
        )
    }

    private suspend fun getPopularShows() : PopularShows = withContext(Dispatchers.IO) {
        PopularShows(
            async { moviesRepository.loadPopularMovies() }.await().results ?: ArrayList(),
            async { tVsRepository.loadPopularTVs() }.await().results ?: ArrayList()
        )
    }

    private suspend fun getTodayShows() : TodayShows = withContext(Dispatchers.IO) {
        TodayShows(
            async { moviesRepository.loadTodayMovies() }.await().results ?: ArrayList(),
            async { tVsRepository.loadTodayTVs() }.await().results ?: ArrayList()
        )
    }

    fun addToWatchlist(response: SaveMovieResponse) {
        savingState.postValue(SavingState.Saving())
        compositeDisposable.add(dbRepository.getMovieByID(response.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(it.isNotEmpty()) { savingState.postValue(SavingState.AlreadyExist(it.first().title)) }
                    else { saveToWatchlist(response) }
                }, {
                    savingState.postValue(SavingState.Error(it.localizedMessage ?: "Error occured while saving"))
                }))
    }

    private fun saveToWatchlist(response: SaveMovieResponse) {
        when(response.type) {
            BaseDetails.ShowType.TYPE_TV -> {
                compositeDisposable.add(tVsRepository.loadTVDetails(response.id)
                    .zipWith(tVsRepository.loadTVsKeywords(response.id), {t1, t2 ->
                        t1.apply { t2.keywords?.let { keywords = it.keywordsToString() } }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it.posterBytes = response.poster
                        it.posterBackBytes = response.backPoster
                        saveToDB(it.toDBMovies())
                    }, {
                        savingState.postValue(SavingState.Error(it.localizedMessage ?: "Error occured while saving"))
                    }))
            }
            BaseDetails.ShowType.TYPE_MOVIE -> {
                compositeDisposable.add(moviesRepository.loadMoviesDetails(id = response.id)
                    .zipWith(moviesRepository.loadMoviesKeywords(response.id), {t1, t2 ->
                        t1.apply { t2.keywords?.let { keywords = it.keywordsToString() } }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it.posterBytes = response.poster
                        it.posterBackBytes = response.backPoster
                        saveToDB(it.toDBMovies())
                    }, {
                        savingState.postValue(SavingState.Error(it.localizedMessage ?: "Error occured while saving"))
                    }))
            }
        }
    }
    private fun saveToDB(dbMovie: DBMovies) {
        compositeDisposable.add(
                dbRepository.saveToWatchlist(dbMovie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    savingState.postValue(SavingState.Saved(dbMovie.title))
                }, {
                    savingState.postValue(SavingState.Error(it.localizedMessage ?: "Error occured while saving"))
                })
        )
    }

    override fun onCleared() {
        coroutineContext.cancelChildren()
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

}

class MainViewModelFactory @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val tvsRepository: TVsRepository,
    private val dbRepository: DBRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(moviesRepository, tvsRepository, dbRepository) as T
    }
}