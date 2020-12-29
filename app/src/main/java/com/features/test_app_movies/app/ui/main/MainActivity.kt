package com.features.test_app_movies.app.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.features.test_app_movies.R
import com.features.test_app_movies.app.customViews.SwitchButtonView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv_today_on_main = findViewById<RecyclerView>(R.id.rv_today_on_main)
        rv_today_on_main.layoutManager = LinearLayoutManager(this)
//        rv_today_on_main.adapter =

        val rv_whats_popular = findViewById<RecyclerView>(R.id.rv_whats_popular_main)
        rv_whats_popular.layoutManager = LinearLayoutManager(this)
//        rv_whats_popular.adapter =

        val rv_top_rated = findViewById<RecyclerView>(R.id.rv_top_rated_main)
        rv_top_rated.layoutManager = LinearLayoutManager(this)
//        rv_top_rated.adapter =

        findViewById<SwitchButtonView>(R.id.sbv_today_on_main)
            .listener.observe(this, Observer { type ->
                // TODO
            })

        findViewById<SwitchButtonView>(R.id.sbv_whats_popular_main)
            .listener.observe(this, Observer { type ->
                // TODO
            })

        findViewById<SwitchButtonView>(R.id.sbv_top_rated_main)
            .listener.observe(this, Observer { type ->
                // TODO
            })

    }
}