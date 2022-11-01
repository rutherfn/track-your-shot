package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.my.shot.navigation.NavigatorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {

    val modules = module {
        single<BuildType> {
            BuildTypeImpl(buildTypeValue = BuildConfig.BUILD_TYPE)
        }
        single<AppCenter> {
            AppCenterImpl(application = androidApplication(), buildType = get())
        }
        single<Navigator> {
            NavigatorImpl()
        }
        viewModel {
            MainActivityViewModel(appCenter = get())
        }
        viewModel {
            SplashViewModel(navigator = get())
        }
        viewModel {
            HomeViewModel(navigator = get())
        }
    }
}
