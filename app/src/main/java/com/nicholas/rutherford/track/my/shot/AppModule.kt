package com.nicholas.rutherford.track.my.shot

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepositoryImpl
import com.nicholas.rutherford.track.my.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.UserRepositoryImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeNavigation
import com.nicholas.rutherford.track.my.shot.feature.home.HomeNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigationImpl
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseLastUpdated
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseLastUpdatedImpl
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfoImpl
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.firebase.util.authentication.AuthenticationFirebaseImpl
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebaseImpl
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
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
            getSharedPreferences(androidApplication())
        }
        single {
            Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                Constants.APP_DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
        single {
            get<AppDatabase>().activeUserDao()
        }
        single {
            get<AppDatabase>().userDao()
        }
        single<ActiveUserRepository> {
            ActiveUserRepositoryImpl(activeUserDao = get())
        }
        single<UserRepository> {
            UserRepositoryImpl(userDao = get())
        }
        single<android.content.SharedPreferences.Editor> {
            getSharedPreferences(androidApplication()).edit()
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
        single<CreateFirebaseLastUpdated> {
            CreateFirebaseLastUpdatedImpl(createFirebaseLastUpdated = get(), firebaseDatabase = get())
        }
        single<AuthenticationFirebase> {
            AuthenticationFirebaseImpl(firebaseAuth = get())
        }
        single<ExistingUserFirebase> {
            ExistingUserFirebaseImpl(firebaseAuth = get())
        }
        single<ReadFirebaseUserInfo> {
            ReadFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
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
        single<HomeNavigation> {
            HomeNavigationImpl(navigator = get())
        }
        viewModel {
            MainActivityViewModel(appCenter = get())
        }
        viewModel {
            SplashViewModel(
                navigation = get(),
                readFirebaseUserInfo = get(),
                activeUserRepository = get(),
                userRepository = get()
            )
        }
        viewModel {
            LoginViewModel(
                application = androidApplication(),
                existingUserFirebase = get(),
                navigation = get(),
                buildType = get(),
                activeUserRepository = get(),
                userRepository = get()
            )
        }
        viewModel {
            HomeViewModel(
                navigation = get(),
                existingUserFirebase = get(),
                activeUserRepository = get()
            )
        }
        viewModel {
            ForgotPasswordViewModel(
                application = androidApplication(),
                authenticationFirebase = get(),
                navigation = get()
            )
        }
        viewModel {
            CreateAccountViewModel(
                navigation = get(),
                application = androidApplication(),
                network = get(),
                createFirebaseUserInfo = get(),
                authenticationFirebase = get(),
                userRepository = get()
            )
        }
        viewModel {
            AuthenticationViewModel(
                readFirebaseUserInfo = get(),
                navigation = get(),
                application = androidApplication(),
                authenticationFirebase = get(),
                createFirebaseUserInfo = get(),
                activeUserRepository = get()
            )
        }
    }

    private fun getSharedPreferences(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }
}
