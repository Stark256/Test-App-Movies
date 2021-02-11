package com.features.test_app_movies.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.features.test_app_movies.api.ApiService
import com.features.test_app_movies.api.models.*
import com.features.test_app_movies.app.common.*
import com.features.test_app_movies.app.customViews.SwitchButtonView
import com.features.test_app_movies.app.models.SaveMovieResponse
import com.features.test_app_movies.app.models.SavingState
import com.features.test_app_movies.db.AppDatabase
import com.features.test_app_movies.db.models.DBMovies
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val api: ApiService, private val db: AppDatabase) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, e ->  throw e}

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
            async { api.getTopRatedMovies() }.await().results ?: ArrayList(),
            async { api.getTopRatedTVs() }.await().results ?: ArrayList()
        )
    }

    private suspend fun getPopularShows() : PopularShows = withContext(Dispatchers.IO) {
        PopularShows(
            async { api.getPopularMovies() }.await().results ?: ArrayList(),
            async { api.getPopularTVs() }.await().results ?: ArrayList()
        )
    }

    private suspend fun getTodayShows() : TodayShows = withContext(Dispatchers.IO) {
        TodayShows(
            async { api.getTodayMovies() }.await().results ?: ArrayList(),
            async { api.getTodayTVs() }.await().results ?: ArrayList()
        )
    }

    fun addToWatchlist(response: SaveMovieResponse) {
        savingState.postValue(SavingState.Saving())
        compositeDisposable.add(RxJavaBridge.toV3Single(db.getMoviesDao().getMovieByID(response.id))
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
                compositeDisposable.add(api.getTVDetails(id = response.id)
                    .zipWith(api.getTVKeywords(response.id), {t1, t2 ->
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
                compositeDisposable.add(api.getMovieDetails(id = response.id)
                    .zipWith(api.getMovieKeywords(response.id), {t1, t2 ->
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
            RxJavaBridge.toV3Completable(db.getMoviesDao().insert(dbMovie))
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