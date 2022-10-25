package com.nicholas.rutherford.track.my.shot

import android.app.Application
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {

    val modules = module {
        single<AppCenter> {
            appCenterImpl(application = androidApplication())
        }
        single {
            buildTypeImpl()
        }
        single {
            buildNavigator()
        }
        viewModel {
            MainActivityViewModel(appCenter = appCenterImpl(androidApplication()))
        }
        viewModel {
            SplashViewModel(navigator = buildNavigator())
        }
        viewModel {
            HomeViewModel(navigator = buildNavigator())
        }
    }

    internal fun buildTypeImpl(): BuildTypeImpl = BuildTypeImpl(buildTypeValue = BuildConfig.BUILD_TYPE)

    internal fun appCenterImpl(application: Application): AppCenterImpl = AppCenterImpl(
        application = application,
        buildType = buildTypeImpl()
    )

    private fun buildNavigator(): Navigator = Navigator()
}
