package com.features.test_app_movies.app.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.features.test_app_movies.R

class MainAdapter : RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {

    private val dataArr = ArrayList<String>()
    private lateinit var context: Context


    fun replaceAll(arr: ArrayList<String>) {
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

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        // TODO bind view
    }

    override fun getItemCount(): Int {
        return dataArr.size
    }

    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    }
}