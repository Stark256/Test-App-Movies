package com.features.test_app_movies.app.ui.watchlist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.features.test_app_movies.R
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.app.common.BaseActivity
import com.features.test_app_movies.app.ui.details.DetailsActivity
import com.features.test_app_movies.databinding.ActivityWatchlistBinding
import com.features.test_app_movies.db.models.DBMovies
import javax.inject.Inject

class WatchlistActivity : BaseActivity() {

    lateinit var watchlistComponent: WatchlistComponent
    @Inject lateinit var factory: WatchlistViewModelFactory
    private lateinit var viewModel: WatchListViewModel
    private val binding: ActivityWatchlistBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_watchlist) }
    private lateinit var adapter: WatchlistAdapter

    private var clickEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        watchlistComponent = appComponent.watchlistComponent().create()
        watchlistComponent.inject(this)
        super.onCreate(savedInstanceState)

        this.viewModel = ViewModelProvider(viewModelStore, factory).get(WatchListViewModel::class.java)


        this.adapter = WatchlistAdapter(object : WatchlistAdapter.ItemClickListener {
            override fun onItemPressed(item: DBMovies, type: BaseDetails.ShowType) {
                if(clickEnabled) {
                    startActivity(DetailsActivity.newInstance(this@WatchlistActivity, item.movieID ?: 0, type))
                }
            }

            override fun onPopupPressed(item: DBMovies, view: View) {
                if(clickEnabled) {
                    showPopupMenu(view) {
                        viewModel.removeFromWatchlist(item)
                    }
                }
            }
        })

        setSupportActionBar(binding.toolbarWatchlist)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        binding.rvWatchlist.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        binding.rvWatchlist.adapter = adapter

        this.viewModel.apply {
            state.observe(this@WatchlistActivity, { state ->
                setState(state)
            })
            watchlistLiveData.observe(this@WatchlistActivity, { watchlist ->
               adapter.replaceAll(watchlist)
            })
            loadData()
        }

    }

    private fun setState(state: WatchListState) {
        when(state) {
            is WatchListState.DefaultState -> {
                binding.tvErrorWatchlist.visibility = View.GONE
                binding.pbWatchlist.visibility = View.GONE
                binding.rvWatchlist.visibility = View.GONE
                clickEnabled = false
            }
            is WatchListState.LoadingState -> {
                binding.tvErrorWatchlist.visibility = View.GONE
                binding.pbWatchlist.visibility = View.VISIBLE
                binding.rvWatchlist.visibility = View.GONE
                clickEnabled = false
            }
            is WatchListState.ErrorState -> {
                binding.tvErrorWatchlist.visibility = View.VISIBLE
                binding.tvErrorWatchlist.text = state.message
                binding.pbWatchlist.visibility = View.GONE
                binding.rvWatchlist.visibility = View.GONE
                clickEnabled = false
            }
            is WatchListState.Success -> {
                binding.tvErrorWatchlist.visibility = View.GONE
                binding.pbWatchlist.visibility = View.GONE
                binding.rvWatchlist.visibility = View.VISIBLE
                clickEnabled = true
            }
        }
    }

    private fun showPopupMenu(view: View, removeFromWatchlist: () -> Unit) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.popup_menu_remove, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.remove_item_menu -> { removeFromWatchlist.invoke() }
            }
            false
        }
        popup.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}