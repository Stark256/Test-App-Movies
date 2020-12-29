package com.features.test_app_movies.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.features.test_app_movies.R

class PopularityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr){

    private var root: CardView? = null
    private var progressBar: ProgressBar? = null
    private var textView: TextView? = null

    init {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.view_popularity, this, true)

        this.root = view.findViewById(R.id.root_popularity)
        this.progressBar = view.findViewById(R.id.pb_popularity)
        this.textView = view.findViewById(R.id.tv_popularity_percentage)

    }

    fun setPercentage(value: Int) {
        when {
            value == 0 -> {
                this.progressBar?.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.progress_circle_grey)
                this.textView?.text = context.getString(R.string.not_rated)
                this.progressBar?.progress = 0
            }
            value < 25 -> {
                this.progressBar?.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.progress_circle_red)
                this.textView?.text = value.toString()
                this.progressBar?.progress = value
            }
            value in 25..69 -> {
                this.progressBar?.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.progress_circle_yellow)
                this.textView?.text = value.toString()
                this.progressBar?.progress = value
            }
            value >= 70 -> {
                this.progressBar?.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.progress_circle_green)
                this.textView?.text = value.toString()
                this.progressBar?.progress = value
            }
        }
    }



}