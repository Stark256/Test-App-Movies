package com.features.test_app_movies.app.ui.details

import android.app.Activity
import com.features.test_app_movies.app.common.putExtra
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DetailActivityDelegate<T: Any>: ReadWriteProperty<Activity, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        val key = property.name
        return thisRef.intent.extras?.get(key) as? T
                ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        val key = property.name
        thisRef.intent.putExtra(key, value)
    }
}