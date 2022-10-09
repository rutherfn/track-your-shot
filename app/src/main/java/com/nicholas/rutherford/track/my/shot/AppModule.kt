package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppCenter> {
        AppCenterImpl(application = androidApplication(), buildType = BuildTypeImpl(buildTypeValue = BuildConfig.BUILD_TYPE))
    }
    single {
        BuildTypeImpl(buildTypeValue = get())
    }
    viewModel {
        MainActivityViewModel(appCenter = get())
    }
}
