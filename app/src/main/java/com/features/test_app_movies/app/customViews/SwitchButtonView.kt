package com.features.test_app_movies.app.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.features.test_app_movies.R
import com.google.android.material.button.MaterialButton
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

class SwitchButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private var root: LinearLayout? = null
    private var btnTV: MaterialButton? = null
    private var btnTheatres: MaterialButton? = null

    private var switchType : SwitchType = SwitchType.TYPE_TV

//    private lateinit var listener: SwitchButtonTypeChangeListener
    val listener =  MutableLiveData<SwitchType>()

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_switch_button, this, true)

        this.root = view.findViewById(R.id.ll_root_switch)
        this.btnTV = view.findViewById(R.id.btn_tv)
        this.btnTheatres = view.findViewById(R.id.btn_theatres)

        this.btnTV?.clicks()
            ?.filter{ switchType != SwitchType.TYPE_TV }
            ?.debounce(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                switchType = SwitchType.TYPE_TV
                customizeButtons()
                listener.value = switchType
//                if(::listener.isInitialized) {
//                    listener.onTypeChanged(SwitchType.TYPE_THEATRES)
//                }
        }

        this.btnTheatres?.clicks()
            ?.filter{ switchType != SwitchType.TYPE_THEATRES }
            ?.debounce(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                switchType = SwitchType.TYPE_THEATRES
                customizeButtons()
                listener.value = switchType
//                if(::listener.isInitialized) {
//                    listener.onTypeChanged(SwitchType.TYPE_TV)
//                }
        }

        customizeButtons()
    }

    private fun customizeButtons() {
        when(switchType) {
            SwitchType.TYPE_TV -> {
                this.btnTV?.setTextColor(ContextCompat.getColor(context, R.color.sunrise_accent))
                this.btnTV?.background?.setTint(ContextCompat.getColor(context, R.color.white))

                this.btnTheatres?.setTextColor(ContextCompat.getColor(context, R.color.white))
                this.btnTheatres?.background?.setTint(ContextCompat.getColor(context, R.color.transparent))
            }
            SwitchType.TYPE_THEATRES -> {
                this.btnTV?.setTextColor(ContextCompat.getColor(context, R.color.white))
                this.btnTV?.background?.setTint(ContextCompat.getColor(context, R.color.transparent))

                this.btnTheatres?.setTextColor(ContextCompat.getColor(context, R.color.sunrise_accent))
                this.btnTheatres?.background?.setTint(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

//    fun setSwitchButtonTypeChangeListener(listener: SwitchButtonTypeChangeListener) {
//        this.listener = listener
//    }

//    interface SwitchButtonTypeChangeListener {
//        fun onTypeChanged(switchType: SwitchType)
//    }

    enum class SwitchType {
        TYPE_TV, TYPE_THEATRES
    }
}