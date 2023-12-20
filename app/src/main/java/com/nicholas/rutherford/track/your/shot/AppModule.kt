package com.nicholas.rutherford.track.your.shot

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.app.center.AppCenter
import com.nicholas.rutherford.track.your.shot.app.center.AppCenterImpl
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepositoryImpl
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.your.shot.feature.login.LoginNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdatesImpl
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseLastUpdated
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseLastUpdatedImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebaseImpl
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebaseImpl
import com.nicholas.rutherford.track.your.shot.helper.account.AccountAuthManager
import com.nicholas.rutherford.track.your.shot.helper.account.AccountAuthManagerImpl
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.helper.network.NetworkImpl
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.NavigatorImpl
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferencesImpl
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferencesImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {
    val mainCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val ioCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val defaultCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
        single {
            get<AppDatabase>().playerDao()
        }
        single<ActiveUserRepository> {
            ActiveUserRepositoryImpl(activeUserDao = get())
        }
        single<UserRepository> {
            UserRepositoryImpl(userDao = get())
        }
        single<PlayerRepository> {
            PlayerRepositoryImpl(playerDao = get())
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
        single {
            FirebaseStorage.getInstance()
        }
        single<CreateFirebaseUserInfo> {
            CreateFirebaseUserInfoImpl(
                firebaseAuth = get(),
                createFirebaseLastUpdated = get(),
                firebaseStorage = get(),
                firebaseDatabase = get()
            )
        }
        single<CreateFirebaseLastUpdated> {
            CreateFirebaseLastUpdatedImpl(firebaseDatabase = get())
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
        single<UpdateFirebaseUserInfo> {
            UpdateFirebaseUserInfoImpl(firebaseDatabase = get())
        }
        single<DeleteFirebaseUserInfo> {
            DeleteFirebaseUserInfoImpl(firebaseDatabase = get())
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
        single<AccountAuthManager> {
            AccountAuthManagerImpl(
                scope = defaultCoroutineScope,
                application = get(),
                navigator = get(),
                activeUserRepository = get(),
                playerRepository = get(),
                userRepository = get(),
                readFirebaseUserInfo = get(),
                existingUserFirebase = get()
            )
        }
        single<PlayersAdditionUpdates> {
            PlayersAdditionUpdatesImpl()
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
        single<PlayersListNavigation> {
            PlayersListNavigationImpl(navigator = get())
        }
        single<CreateEditPlayerNavigation> {
            CreateEditPlayerNavigationImpl(navigator = get())
        }
        viewModel {
            MainActivityViewModel(appCenter = get(), accountAuthManager = get())
        }
        viewModel {
            SplashViewModel(
                navigation = get(),
                readFirebaseUserInfo = get(),
                activeUserRepository = get(),
            )
        }
        viewModel {
            LoginViewModel(
                application = androidApplication(),
                navigation = get(),
                buildType = get(),
                accountAuthManager = get()
            )
        }
        viewModel {
            PlayersListViewModel(
                application = androidApplication(),
                scope = defaultCoroutineScope,
                navigation = get(),
                network = get(),
                accountAuthManager = get(),
                deleteFirebaseUserInfo = get(),
                activeUserRepository = get(),
                playersAdditionUpdates = get(),
                playerRepository = get()
            )
        }
        viewModel {
            CreateEditPlayerViewModel(
                application = androidApplication(),
                createFirebaseUserInfo = get(),
                updateFirebaseUserInfo = get(),
                readFirebaseUserInfo = get(),
                playerRepository = get(),
                activeUserRepository = get(),
                scope = defaultCoroutineScope,
                navigation = get(),
                playersAdditionUpdates = get(),
                network = get()
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
