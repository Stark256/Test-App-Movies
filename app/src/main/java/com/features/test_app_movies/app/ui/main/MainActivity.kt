package com.features.test_app_movies.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.features.test_app_movies.R
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.app.common.BaseActivity
import com.features.test_app_movies.app.common.ProgressDialog
import com.features.test_app_movies.app.models.SavingState
import com.features.test_app_movies.app.customViews.SwitchButtonView
import com.features.test_app_movies.app.models.SaveMovieResponse
import com.features.test_app_movies.app.ui.details.DetailsActivity
import com.features.test_app_movies.app.ui.watchlist.WatchlistActivity
import com.features.test_app_movies.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.appcompat.itemClicks
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseActivity() {

    lateinit var mainComponent: MainComponent
    @Inject lateinit var factory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var topRatedShowsAdapter: MainAdapter
    private lateinit var popularShowsAdapter: MainAdapter
    private lateinit var todayShowsAdapter: MainAdapter
    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    private var clickEnabled: Boolean = false
    private var progressDialog = ProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = appComponent.mainComponent().create()
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)

        this.viewModel = ViewModelProvider(viewModelStore, this.factory).get(MainViewModel::class.java)

        binding.toolbarMain.inflateMenu(R.menu.menu_main)
        binding.toolbarMain.itemClicks().subscribe {
            when(it.itemId) {
                R.id.watchlist_menu_item -> {
                    startActivity(Intent(this, WatchlistActivity::class.java))
                }
            }
        }
        binding.swipeRefreshMain.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.sunrise_accent)
        )

        binding.swipeRefreshMain.refreshes()
            .debounce(2, TimeUnit.SECONDS)
            .subscribe {
            viewModel.loadData()
        }

        binding.rvTodayOnMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        this.todayShowsAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onItemPressed(id: Long, type: BaseDetails.ShowType) {
                if(clickEnabled) {
                    startActivity(DetailsActivity.newInstance(this@MainActivity, id, type))
                }
            }

            override fun onPopupPressed(response: SaveMovieResponse, view: View) {
                if(clickEnabled) {
                    showPopupMenu(view) {
                        viewModel.addToWatchlist(response)
                    }
                }
            }
        })
        binding.rvTodayOnMain.adapter = todayShowsAdapter

        binding.rvWhatsPopularMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        this.popularShowsAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onItemPressed(id: Long, type: BaseDetails.ShowType) {
                if(clickEnabled) {
                    startActivity(DetailsActivity.newInstance(this@MainActivity, id, type))
                }
            }

            override fun onPopupPressed(response: SaveMovieResponse, view: View) {
                if(clickEnabled) {
                    showPopupMenu(view) {
                        viewModel.addToWatchlist(response)
                    }
                }
            }
        })
        binding.rvWhatsPopularMain.adapter = popularShowsAdapter

        binding.rvTopRatedMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        this.topRatedShowsAdapter = MainAdapter(object : MainAdapter.ItemClickListener {
            override fun onItemPressed(id: Long, type: BaseDetails.ShowType) {
                if(clickEnabled) {
                    startActivity(DetailsActivity.newInstance(this@MainActivity, id, type))
                }
            }

            override fun onPopupPressed(response: SaveMovieResponse, view: View) {
                if(clickEnabled) {
                    showPopupMenu(view) {
                        viewModel.addToWatchlist(response)
                    }
                }
            }
        })
        binding.rvTopRatedMain.adapter = topRatedShowsAdapter

        binding.sbvTodayOnMain.setSwitchButtonTypeChangeListener(object : SwitchButtonView.SwitchButtonTypeChangeListener {
            override fun onTypeChanged(switchType: SwitchButtonView.SwitchType) {
                viewModel.changeTodayType(switchType)
            }
        })

        binding.sbvWhatsPopularMain.setSwitchButtonTypeChangeListener(object : SwitchButtonView.SwitchButtonTypeChangeListener {
            override fun onTypeChanged(switchType: SwitchButtonView.SwitchType) {
                viewModel.changePopularType(switchType)
            }
        })

        binding.sbvTopRatedMain.setSwitchButtonTypeChangeListener(object : SwitchButtonView.SwitchButtonTypeChangeListener {
            override fun onTypeChanged(switchType: SwitchButtonView.SwitchType) {
                viewModel.changeTopRatedType(switchType)
            }
        })


        viewModel.apply {

            state.observe(this@MainActivity, { state ->
                setState(state)
            })
            todayLiveData.observe(this@MainActivity, { list ->
                todayShowsAdapter.replaceAll(list)
            })
            popularLiveData.observe(this@MainActivity, { list ->
                popularShowsAdapter.replaceAll(list)
            })
            topRatedLiveData.observe(this@MainActivity, { list ->
                topRatedShowsAdapter.replaceAll(list)
            })
            savingState.observe(this@MainActivity, { state ->
                setSavingState(state)
            })

            loadData()
        }

    }

    private fun setSavingState(state: SavingState) {
        when (state) {
            is SavingState.Saving -> {
                progressDialog.show(supportFragmentManager, this.localClassName)
            }
            is SavingState.AlreadyExist -> {
                progressDialog.dismiss()
                Toast.makeText(this,
                        String.format(getString(R.string.already_saved_format), state.title),
                        Toast.LENGTH_SHORT)
                        .show()
            }
            is SavingState.Error -> {
                progressDialog.dismiss()
                Toast.makeText(this, state.message,
                        Toast.LENGTH_SHORT).show()
            }
            is SavingState.Saved -> {
                progressDialog.dismiss()
                Toast.makeText(this,
                        String.format(getString(R.string.saved_fortam), state.title),
                        Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    private fun setState(state: MainState) {
        when(state) {
            is MainState.DefaultState -> {
                binding.tvErrorMain.visibility = View.GONE
                binding.swipeRefreshMain.isRefreshing = false
                clickEnabled = false
            }
            is MainState.LoadingState -> {
                binding.tvErrorMain.visibility = View.GONE
                binding.swipeRefreshMain.isRefreshing = true
                binding.sbvTodayOnMain.isClickEnable = false
                binding.sbvWhatsPopularMain.isClickEnable = false
                binding.sbvTopRatedMain.isClickEnable = false
                clickEnabled = false
            }
            is MainState.ErrorState -> {
                binding.swipeRefreshMain.isRefreshing = false
                binding.scrollViewMain.visibility = View.GONE
                binding.tvErrorMain.visibility = View.VISIBLE
                binding.tvErrorMain.text = state.message
                clickEnabled = false
            }
            is MainState.Success -> {
                binding.swipeRefreshMain.isRefreshing = false
                binding.scrollViewMain.visibility = View.VISIBLE
                binding.tvErrorMain.visibility = View.GONE
                binding.sbvTodayOnMain.isClickEnable = true
                binding.sbvWhatsPopularMain.isClickEnable = true
                binding.sbvTopRatedMain.isClickEnable = true
                clickEnabled = true
            }
        }
    }

    private fun showPopupMenu(view: View, addToWatchlist: () -> Unit) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.popup_menu_add, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.add_item_menu -> { addToWatchlist.invoke() }
            }
            false
        }
        popup.show()
    }
}