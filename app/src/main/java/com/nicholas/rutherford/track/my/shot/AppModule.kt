package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        single { AppCenterImpl(application = androidApplication()) }
    }
    viewModel {
        MainActivityViewModel(appCenter = get())
    }
}
