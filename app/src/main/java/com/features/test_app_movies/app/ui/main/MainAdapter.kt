package com.features.test_app_movies.app.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.features.test_app_movies.R
import com.features.test_app_movies.api.models.BaseDetails
import com.features.test_app_movies.api.models.movie_models.MovieDetails
import com.features.test_app_movies.api.models.tv_models.TVDetails
import com.features.test_app_movies.app.common.bitmapToByteArr
import com.features.test_app_movies.app.common.glideListener
import com.features.test_app_movies.app.customViews.PopularityView
import com.features.test_app_movies.app.models.SaveMovieResponse
import com.jakewharton.rxbinding4.view.clicks

class MainAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {

    private val dataArr = ArrayList<BaseDetails>()
    private lateinit var context: Context


    fun replaceAll(arr: ArrayList<BaseDetails>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        this.context = parent.context
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main , parent, false))
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        when(val item = dataArr[position]) {
            is MovieDetails -> {
                val movie = item as MovieDetails

                holder.bindView(movie.title, movie.releaseDate, movie.voteAverage?.times(10))

                Glide.with(context)
                    .asBitmap()
                    .load(String.format(context.getString(R.string.image_url), movie.posterPath))
                    .listener(glideListener({
                        movie.posterBytes = it?.bitmapToByteArr()
                    }, {/*error*/}))
                    .centerCrop()
                    .into(holder.image)

                Glide.with(context)
                    .asBitmap()
                    .load(String.format(context.getString(R.string.image_url), movie.backPath))
                    .listener(glideListener({
                        movie.posterBackBytes = it?.bitmapToByteArr()
                    }, {/*error*/}))
                    .submit()

                holder.itemView
                        .clicks()
                        .subscribe {
                            listener.onItemPressed(movie.id, movie.type)
                        }

                holder.btnPopup
                        .clicks()
                        .subscribe{
                            listener.onPopupPressed(
                                SaveMovieResponse(movie.id, movie.type,
                                    movie.posterBytes, movie.posterBackBytes), holder.btnPopup)
                        }
            }
            is TVDetails -> {
                val tv = item as TVDetails

                holder.bindView(tv.title, tv.firstAirDate, tv.voteAverage?.times(10))


                Glide.with(context)
                    .asBitmap()
                    .load(String.format(context.getString(R.string.image_url), tv.posterPath))
                    .listener(glideListener({
                        tv.posterBytes = it?.bitmapToByteArr()
                    }, {/*error*/}))
                    .centerCrop()
                    .into(holder.image)

                Glide.with(context)
                    .asBitmap()
                    .load(String.format(context.getString(R.string.image_url), tv.backPath))
                    .listener(glideListener({
                        tv.posterBackBytes = it?.bitmapToByteArr()
                    }, {/*error*/}))
                    .submit()


                holder.itemView
                        .clicks()
                        .subscribe {
                            listener.onItemPressed(tv.id, tv.type)
                        }

                holder.btnPopup
                        .clicks()
                        .subscribe{
                            listener.onPopupPressed(
                                SaveMovieResponse(tv.id, tv.type,
                                    tv.posterBytes, tv.posterBackBytes), holder.btnPopup)
                        }

            }
        }
    }

    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tv_title_main)
        val date: TextView = v.findViewById(R.id.tv_date_main)
        val popularity: PopularityView = v.findViewById(R.id.pv_main)
        val image: ImageView = v.findViewById(R.id.iv_image_main)
        val btnPopup: ImageView = v.findViewById(R.id.iv_popup_main)

        fun bindView(title: String?, date: String?, popularity: Float?) {
            this.title.text = title
            this.date.text = date
            this.popularity.setPercentage(popularity?.toInt() ?: 0)
        }

    }

    interface ItemClickListener {
        fun onItemPressed(id: Long, type: BaseDetails.ShowType)
        fun onPopupPressed(response: SaveMovieResponse, view: View)
    }
}