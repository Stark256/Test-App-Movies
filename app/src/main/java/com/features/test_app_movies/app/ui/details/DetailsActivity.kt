package com.features.test_app_movies.app.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.features.test_app_movies.R
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.app.common.BaseActivity
import com.features.test_app_movies.app.common.ProgressDialog
import com.features.test_app_movies.app.common.genresToString
import com.features.test_app_movies.databinding.ActivityDetailsBinding
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class DetailsActivity : BaseActivity() {

//     TODO fix
//    private var param1: Int by extras()
//    private var param2: String by extras()

    companion object {
        private object Extras {
            const val EXTRA_ID = "extra_id"
            const val EXTRA_TYPE = "extra_type"
        }

        fun newInstance(activity: Activity, id: Long, type: BaseDetails.ShowType) : Intent {
            return Intent(activity, DetailsActivity::class.java).apply {
                putExtra(Extras.EXTRA_ID, id)
                putExtra(Extras.EXTRA_TYPE, type.value)
            }
        }
    }

    private val id: Long?
        get() { return intent.extras?.getLong(Extras.EXTRA_ID) }

    private val type: BaseDetails.ShowType?
        get() {
            val value = intent.extras?.getString(Extras.EXTRA_TYPE)
            return  when {
            value is String && value == BaseDetails.ShowType.TYPE_TV.value -> BaseDetails.ShowType.TYPE_TV
            value is String && value == BaseDetails.ShowType.TYPE_MOVIE.value -> BaseDetails.ShowType.TYPE_MOVIE
            else -> null
        } }

    @Inject
    lateinit var factory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: ActivityDetailsBinding

    private var progressDialog = ProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        appComponent.inject(this)

        setSupportActionBar(binding.toolbarDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        this.viewModel = ViewModelProvider(viewModelStore, this.factory).get(DetailsViewModel::class.java)
        this.viewModel.apply {

            state.observe(this@DetailsActivity, { state ->
                setState(state)
            })

            data.observe(this@DetailsActivity, { data ->
                when(data.type) {
                    BaseDetails.ShowType.TYPE_MOVIE -> bindMovieView(data as MovieDetails)
                    BaseDetails.ShowType.TYPE_TV -> bindTVView(data as TVDetails)
                }
            })

            loadDetails(id, type)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { finish() }
        }


        return super.onOptionsItemSelected(item)
    }

    private fun bindMovieView(movie: MovieDetails) {
        binding.tvTitleDetails.text = movie.title
        binding.tvTaglineDetails.text = movie.tagline
        binding.tvOverviewDetails.text = movie.overview
        binding.pvDetailsPopularity.setPercentage(movie.voteAverage?.times(10)?.toInt() ?: 0)
        binding.tvDetailsBudget.text = NumberFormat.getCurrencyInstance(Locale.US).format(movie.budget)
        binding.tvDetailsRevenue.text = NumberFormat.getCurrencyInstance(Locale.US).format(movie.revenue)
        binding.tvDateRuntimeDetails.text =
            String.format(getString(R.string.release_date_format), movie.releaseDate, movie.runtime)
        binding.tvGenreDetails.text = movie.genres?.genresToString() ?: "-"
        binding.tvKeywordsDetails.text = movie.keywords//.keywordsToString() ?: "-"


        Glide.with(this)
            .load(String.format(getString(R.string.image_url), movie.posterPath))
            .centerCrop()
            .into(binding.ivImageDetails)

        Glide.with(this)
            .load(String.format(getString(R.string.image_url), movie.backPath))
            .centerCrop()
            .into(binding.ivImageBackDetails)
    }

    private fun bindTVView(tv: TVDetails) {
        binding.tvTitleDetails.text = tv.title
        binding.tvTaglineDetails.text = tv.tagline
        binding.tvOverviewDetails.text = tv.overview
        binding.pvDetailsPopularity.setPercentage(tv.voteAverage?.times(10)?.toInt() ?: 0)
        binding.tvDetailsBudget.text = "-"
        binding.tvDetailsRevenue.text = "-"
        binding.tvDateRuntimeDetails.text =
            String.format(getString(R.string.release_date_format), tv.firstAirDate, tv.episodeRunTime?.firstOrNull())
        binding.tvGenreDetails.text = tv.genres?.genresToString() ?: "-"
        binding.tvKeywordsDetails.text = tv.keywords//.keywordsToString() ?: "-"

        Glide.with(this)
            .load(String.format(getString(R.string.image_url), tv.posterPath))
            .centerCrop()
            .into(binding.ivImageDetails)

        Glide.with(this)
            .load(String.format(getString(R.string.image_url), tv.backPath))
            .centerCrop()
            .into(binding.ivImageBackDetails)
    }

    private fun setState(state: DetailsState) {
        when(state) {
            is DetailsState.DefaultState -> {
                binding.pbDetails.visibility = View.GONE
                binding.tvErrorDetails.visibility = View.GONE
            }
            is DetailsState.ErrorState -> {
                binding.tvErrorDetails.visibility = View.VISIBLE
                binding.tvErrorDetails.text = state.message
                binding.pbDetails.visibility = View.GONE
                binding.clDetailsContainer.visibility = View.GONE
            }
            is DetailsState.LoadingState -> {
                binding.pbDetails.visibility = View.VISIBLE
                binding.clDetailsContainer.visibility = View.GONE
                binding.tvErrorDetails.visibility = View.GONE

            }
            is DetailsState.Success -> {
                binding.pbDetails.visibility = View.GONE
                binding.tvErrorDetails.visibility = View.GONE
                binding.clDetailsContainer.visibility = View.VISIBLE
            }
        }
    }
}