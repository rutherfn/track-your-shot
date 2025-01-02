package com.nicholas.rutherford.track.your.shot

import android.app.Application
import android.os.Build
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.base.resources.declaredshotsjson.DeclaredShotsJson
import com.nicholas.rutherford.track.your.shot.base.resources.declaredshotsjson.DeclaredShotsJsonImpl
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepositoryImpl
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
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShotImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
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
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManagerImpl
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
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
    private val defaultCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val modules = module {
        single {
            getSharedPreferences(androidApplication())
        }
        single<DeclaredShotsJson> {
            DeclaredShotsJsonImpl(application = get())
        }
        single {
            Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                Constants.APP_DATABASE_NAME
            ).build()
        }
        single {
            get<AppDatabase>().activeUserDao()
        }
        single {
            get<AppDatabase>().declaredShotDao()
        }
        single {
            get<AppDatabase>().userDao()
        }
        single {
            get<AppDatabase>().pendingPlayerDao()
        }
        single {
            get<AppDatabase>().playerDao()
        }
        single<ActiveUserRepository> {
            ActiveUserRepositoryImpl(activeUserDao = get())
        }
        single<DeclaredShotRepository> {
            DeclaredShotRepositoryImpl(
                declaredShotDao = get(),
                declaredShotsJson = get()
            )
        }
        single<CurrentPendingShot> { CurrentPendingShotImpl() }
        single<UserRepository> { UserRepositoryImpl(userDao = get()) }
        single<PlayerRepository> {
            PlayerRepositoryImpl(playerDao = get())
        }
        single<PendingPlayerRepository> {
            PendingPlayerRepositoryImpl(pendingPlayerDao = get())
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
                firebaseStorage = get(),
                firebaseDatabase = get()
            )
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
            UpdateFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }
        single<DeleteFirebaseUserInfo> {
            DeleteFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }
        single<Network> {
            NetworkImpl(scope = defaultCoroutineScope)
        }
        single<BuildType> {
            BuildTypeImpl(
                sdkValue = Build.VERSION.SDK_INT,
                buildTypeValue = BuildConfig.BUILD_TYPE
            )
        }
        single<Navigator> {
            NavigatorImpl()
        }
        single<AccountManager> {
            AccountManagerImpl(
                scope = defaultCoroutineScope,
                application = get(),
                navigator = get(),
                activeUserRepository = get(),
                declaredShotRepository = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                userRepository = get(),
                readFirebaseUserInfo = get(),
                existingUserFirebase = get(),
                createSharedPreferences = get()
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
        single<SelectShotNavigation> {
            SelectShotNavigationImpl(navigator = get())
        }
        single<LogShotNavigation> {
            LogShotNavigationImpl(navigator = get())
        }
        single<SettingsNavigation> {
            SettingsNavigationImpl(navigator = get())
        }
        single<PermissionEducationNavigation> {
            PermissionEducationNavigationImpl(navigator = get())
        }
        single<TermsConditionsNavigation> {
            TermsConditionsNavigationImpl(navigator = get())
        }
        single<EnabledPermissionsNavigation> {
            EnabledPermissionsNavigationImpl(navigator = get())
        }
        single<OnboardingEducationNavigation> {
            OnboardingEducationNavigationImpl(navigator = get())
        }
        single<AccountInfoNavigation> {
            AccountInfoNavigationImpl(navigator = get())
        }
        viewModel {
            MainActivityViewModel(accountManager = get())
        }
        viewModel {
            SplashViewModel(
                navigation = get(),
                readFirebaseUserInfo = get(),
                activeUserRepository = get(),
                accountManager = get(),
                readSharedPreferences = get(),
                createSharedPreferences = get()
            )
        }
        viewModel {
            LoginViewModel(
                application = androidApplication(),
                navigation = get(),
                buildType = get(),
                accountManager = get(),
                scope = defaultCoroutineScope
            )
        }
        viewModel {
            PlayersListViewModel(
                application = androidApplication(),
                scope = defaultCoroutineScope,
                navigation = get(),
                network = get(),
                accountManager = get(),
                deleteFirebaseUserInfo = get(),
                playersAdditionUpdates = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                createSharedPreferences = get(),
                readSharedPreferences = get()
            )
        }
        viewModel {
            CreateEditPlayerViewModel(
                application = androidApplication(),
                deleteFirebaseUserInfo = get(),
                createFirebaseUserInfo = get(),
                updateFirebaseUserInfo = get(),
                readFirebaseUserInfo = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                activeUserRepository = get(),
                scope = defaultCoroutineScope,
                navigation = get(),
                playersAdditionUpdates = get(),
                currentPendingShot = get(),
                network = get()
            )
        }
        viewModel {
            SelectShotViewModel(
                application = get(),
                scope = defaultCoroutineScope,
                navigation = get(),
                declaredShotRepository = get(),
                accountManager = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                createSharedPreferences = get(),
                readSharedPreferences = get()
            )
        }
        viewModel {
            LogShotViewModel(
                application = androidApplication(),
                scope = defaultCoroutineScope,
                navigation = get(),
                declaredShotRepository = get(),
                pendingPlayerRepository = get(),
                playerRepository = get(),
                deleteFirebaseUserInfo = get(),
                currentPendingShot = get()
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
                scope = defaultCoroutineScope
            )
        }
        viewModel {
            AuthenticationViewModel(
                readFirebaseUserInfo = get(),
                navigation = get(),
                application = androidApplication(),
                authenticationFirebase = get(),
                createFirebaseUserInfo = get(),
                activeUserRepository = get(),
                createSharedPreferences = get(),
                scope = defaultCoroutineScope
            )
        }
        viewModel {
            SettingsViewModel(
                navigation = get(),
                application = androidApplication(),
                scope = defaultCoroutineScope,
                activeUserRepository = get()
            )
        }
        viewModel {
            PermissionEducationViewModel(
                navigation = get(),
                buildType = get(),
                application = androidApplication()
            )
        }
        viewModel {
            OnboardingEducationViewModel(
                navigation = get(),
                application = androidApplication()
            )
        }
        viewModel {
            TermsConditionsViewModel(
                navigation = get(),
                application = androidApplication(),
                createSharedPreferences = get(),
                scope = defaultCoroutineScope
            )
        }
        viewModel {
            EnabledPermissionsViewModel(
                navigation = get(),
                application = androidApplication()
            )
        }
        viewModel {
            AccountInfoViewModel(navigation = get())
        }
    }

    private fun getSharedPreferences(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(Constants.Preferences.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }
}
