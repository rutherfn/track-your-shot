package com.nicholas.rutherford.track.my.shot

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MyApplication : Application() {

    //  private lateinit var appDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        //  appDatabase = AppDatabase.getInstance(this@MyApplication)
        startKoinOnCreate()
    }

    fun startKoinOnCreate() {
        startKoin {
            androidContext(this@MyApplication)
            modules(AppModule().modules)
        }
    }
}
