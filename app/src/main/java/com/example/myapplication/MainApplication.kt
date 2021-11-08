package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
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
//            val d = Thread.getDefaultUncaughtExceptionHandler()
//            Thread.setDefaultUncaughtExceptionHandler { t, e ->
//                Timber.e(e)
//                d?.uncaughtException(t, e)
//            }
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        builder.setLogLevel(Log.VERBOSE)
    }
}