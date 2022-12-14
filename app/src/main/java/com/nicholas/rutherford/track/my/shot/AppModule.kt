package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.CreateAccountNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigationImpl
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
        single<SplashNavigation> {
            SplashNavigationImpl(navigator = get())
        }
        single<LoginNavigation> {
            LoginNavigationImpl(navigator = get())
        }
        single<ForgotPasswordNavigation> {
            ForgotPasswordNavigationImpl(navigator = get())
        }
        single<CreateAccountNavigation> {
            CreateAccountNavigationImpl(navigator = get())
        }
        viewModel {
            MainActivityViewModel(appCenter = get())
        }
        viewModel {
            SplashViewModel(navigation = get())
        }
        viewModel {
            LoginViewModel(navigation = get(), buildType = get())
        }
        viewModel {
            HomeViewModel(navigator = get())
        }
        viewModel {
            ForgotPasswordViewModel(navigation = get())
        }
        viewModel {
            CreateAccountViewModel(navigation = get())
        }
    }
}
