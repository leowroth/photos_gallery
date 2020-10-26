package com.github.weg_li_android

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class MainApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}