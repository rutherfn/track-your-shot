package com.nicholas.ruitherford.track.my.shot

import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.BuildConfig
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebaseImpl
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebaseImpl
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.my.shot.helper.network.NetworkImpl
import com.nicholas.rutherford.track.my.shot.navigation.NavigatorImpl
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferencesImpl
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferencesImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class TestSetup {

    fun loadAllKoinModules() {
        val modules = module {
            single { NavigatorImpl() }
            single { ReadSharedPreferencesImpl(sharedPreferences = get()) }
            single { SplashNavigationImpl(navigator = get()) }
            single { FirebaseAuth.getInstance() }
            single { FirebaseDatabase.getInstance() }
            single { ReadFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get()) }
            viewModel {
                SplashViewModel(
                    readSharedPreferences = get(),
                    navigation = get(),
                    readFirebaseUserInfo = get()
                )
            }
            single { ExistingUserFirebaseImpl(firebaseAuth = get()) }
            single { LoginNavigationImpl(navigator = get()) }
            single { BuildTypeImpl(buildTypeValue = BuildConfig.BUILD_TYPE) }
            viewModel {
                LoginViewModel(
                    application = androidApplication(),
                    existingUserFirebase = get(),
                    navigation = get(),
                    buildType = get()
                )
            }
            single { HomeNavigationImpl(navigator = get()) }
            viewModel {
                HomeViewModel(
                    navigation = get(),
                    existingUserFirebase = get()
                )
            }
            single { ForgotPasswordNavigationImpl(navigator = get()) }
            single { AuthenticationFirebaseImpl(firebaseAuth = get()) }
            viewModel {
                ForgotPasswordViewModel(
                    application = androidApplication(),
                    authenticationFirebase = get(),
                    navigation = get()
                )
            }
            single { CreateAccountNavigationImpl(navigator = get()) }
            single { NetworkImpl() }
            single { CreateFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get()) }
            viewModel {
                CreateAccountViewModel(
                    navigation = get(),
                    application = androidApplication(),
                    network = get(),
                    createFirebaseUserInfo = get(),
                    authenticationFirebase = get()
                )
            }
            single { AuthenticationNavigationImpl(navigator = get()) }
            single {
                getSharedPreferences(androidApplication())
            }

            single<SharedPreferences.Editor> {
                getSharedPreferences(androidApplication()).edit()
            }
            single { CreateSharedPreferencesImpl(editor = get()) }
            viewModel {
                AuthenticationViewModel(
                    readFirebaseUserInfo = get(),
                    navigation = get(),
                    application = androidApplication(),
                    authenticationFirebase = get(),
                    createSharedPreferences = get(),
                    createFirebaseUserInfo = get(),
                )
            }
        }

        loadKoinModules(modules)
    }

    fun tearDownKoin() = stopKoin()

    private fun getSharedPreferences(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }
}
