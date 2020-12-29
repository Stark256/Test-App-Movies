package com.features.test_app_movies.app.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.features.test_app_movies.R
import com.features.test_app_movies.app.customViews.PopularityView

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //String.format(context.getString(R.string.query_create_table), tableName, fieldsString)

        findViewById<PopularityView>(R.id.pv_popularity).setPercentage(43)
    }
}