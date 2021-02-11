package com.features.test_app_movies.app.ui.watchlist

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
import com.features.test_app_movies.app.common.toShowType
import com.features.test_app_movies.app.customViews.PopularityView
import com.features.test_app_movies.db.models.DBMovies
import com.jakewharton.rxbinding4.view.clicks

class WatchlistAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {
    private lateinit var context: Context
    private val dataArr = ArrayList<DBMovies>()

    fun replaceAll(arr: List<DBMovies>) {
        dataArr.clear()
        if(arr.isNotEmpty()) {
            dataArr.addAll(arr)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int { return dataArr.size }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        this.context = parent.context
        return WatchlistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_watchlist, parent, false))
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val item = dataArr[position]
        holder.bindView(item.title, item.releaseDate, item.voteAverage?.times(10))

        Glide.with(context)
                .load(item.posterBytes)
                .centerCrop()
                .into(holder.image)

        holder.itemView
                .clicks()
                .subscribe {
                    listener.onItemPressed(item, toShowType(item.type))
                }

        holder.btnPopup
                .clicks()
                .subscribe{
                    listener.onPopupPressed(item, holder.btnPopup)
                }
    }


    class WatchlistViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tv_title_watchlist)
        val date: TextView = v.findViewById(R.id.tv_date_watchlist)
        val popularity: PopularityView = v.findViewById(R.id.pv_watchlist)
        val image: ImageView = v.findViewById(R.id.iv_image_watchlist)
        val btnPopup: ImageView = v.findViewById(R.id.iv_popup_watchlist)

        fun bindView(title: String?, date: String?, popularity: Float?) {
            this.title.text = title
            this.date.text = date
            this.popularity.setPercentage(popularity?.toInt() ?: 0)
        }
    }

    interface ItemClickListener {
        fun onItemPressed(item: DBMovies, type: BaseDetails.ShowType)
        fun onPopupPressed(item: DBMovies, view: View)
    }
}