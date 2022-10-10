package com.nicholas.rutherford.track.my.shot

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoinOnCreate()
    }

    fun startKoinOnCreate() {
        startKoin {
            androidContext(this@MyApplication)
            modules(AppModule().modules)
        }
    }
}
