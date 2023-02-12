package com.nicholas.rutherford.track.my.shot

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
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
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebaseImpl
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import com.nicholas.rutherford.track.my.shot.helper.network.NetworkImpl
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.my.shot.navigation.NavigatorImpl
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferencesImpl
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferencesImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {

    val modules = module {
        single {
            getSharedPrefs(androidApplication())
        }

        single<android.content.SharedPreferences.Editor> {
            getSharedPrefs(androidApplication()).edit()
        }
        single<CreateSharedPreferences> {
            CreateSharedPreferencesImpl(editor = get())
        }
        single<ReadSharedPreferences> {
            ReadSharedPreferencesImpl(sharedPreferences = get())
        }
        single {
            FirebaseAuth.getInstance()
        }
        single {
            FirebaseDatabase.getInstance()
        }
        single<CreateFirebaseUserInfo> {
            CreateFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }
        single<AuthenticationFirebase> {
            AuthenticationFirebaseImpl(firebaseAuth = get())
        }
        single<ReadFirebaseUserInfo> {
            ReadFirebaseUserInfoImpl(firebaseAuth = get())
        }
        single<Network> {
            NetworkImpl()
        }
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
        single<AuthenticationNavigation> {
            AuthenticationNavigationImpl(navigator = get())
        }
        viewModel {
            MainActivityViewModel(appCenter = get())
        }
        viewModel {
            SplashViewModel(readSharedPreferences = get(), navigation = get(), readFirebaseUserInfo = get())
        }
        viewModel {
            LoginViewModel(navigation = get(), buildType = get())
        }
        viewModel {
            HomeViewModel(navigator = get(), createSharedPreferences = get())
        }
        viewModel {
            ForgotPasswordViewModel(navigation = get())
        }
        viewModel {
            CreateAccountViewModel(
                navigation = get(),
                application = androidApplication(),
                network = get(),
                createFirebaseUserInfo = get(),
                authenticationFirebase = get()
            )
        }
        viewModel {
            AuthenticationViewModel(
                readFirebaseUserInfo = get(),
                navigation = get(),
                application = androidApplication(),
                authenticationFirebase = get(),
                createSharedPreferences = get(),
                createFirebaseUserInfo = get()
            )
        }
    }

    fun getSharedPrefs(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }
}
