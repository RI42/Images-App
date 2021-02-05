package com.example.myapplication

import android.app.Application
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
        }

//        configCoil()
    }

//    private fun configCoil() {
//        val okHttpClient = OkHttpClient.Builder()
//            .callTimeout(3, TimeUnit.MINUTES)
//            .cache(CoilUtils.createDefaultCache(this))
//            .build()
//
//        val imageLoader = ImageLoader.Builder(this)
//            .okHttpClient(okHttpClient)
//            .crossfade(true)
//            .build()
//
//        Coil.setImageLoader(imageLoader)
//    }
}
