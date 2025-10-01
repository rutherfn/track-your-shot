package com.nicholas.rutherford.track.your.shot.koin

import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.MainActivityViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for providing all ViewModels in the app.
 *
 * This module defines how each ViewModel should be instantiated, including
 * dependencies such as repositories, navigation handlers, shared preferences,
 * Firebase helpers, and coroutine scopes.
 */
object ViewModelsModule {

    /** Koin module definitions for all ViewModels */
    val modules = module {

        /** MainActivity ViewModel providing network and account manager functionality */
        viewModel {
            MainActivityViewModel(
                accountManager = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                network = get(),
                dataStorePreferenceReader = get()
            )
        }

        /** Splash screen ViewModel handling initialization and Firebase user checks */
        viewModel {
            SplashViewModel(
                navigation = get(),
                readFirebaseUserInfo = get(),
                activeUserRepository = get(),
                accountManager = get(),
                dataStorePreferencesReader = get(),
                dataStorePreferencesWriter = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Login screen ViewModel */
        viewModel {
            LoginViewModel(
                application = androidApplication(),
                navigation = get(),
                buildType = get(),
                accountManager = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Players list screen ViewModel */
        viewModel {
            PlayersListViewModel(
                application = androidApplication(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                navigation = get(),
                deleteFirebaseUserInfo = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                databaseStorePreferenceWriter = get()
            )
        }

        /** Create/Edit Player screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            CreateEditPlayerViewModel(
                savedStateHandle = stateHandle,
                application = androidApplication(),
                deleteFirebaseUserInfo = get(),
                createFirebaseUserInfo = get(),
                updateFirebaseUserInfo = get(),
                playerRepository = get(),
                pendingPlayerRepository = get(),
                activeUserRepository = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                navigation = get(),
                currentPendingShot = get()
            )
        }

        /** Select Shot screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            SelectShotViewModel(
                savedStateHandle = stateHandle,
                application = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                navigation = get(),
                declaredShotRepository = get(),
                playerRepository = get(),
                pendingPlayerRepository = get()
            )
        }

        /** Log Shot screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            LogShotViewModel(
                savedStateHandle = stateHandle,
                application = androidApplication(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                navigation = get(),
                declaredShotRepository = get(),
                pendingPlayerRepository = get(),
                playerRepository = get(),
                activeUserRepository = get(),
                updateFirebaseUserInfo = get(),
                deleteFirebaseUserInfo = get(),
                currentPendingShot = get(),
                logShotViewModelExt = get()
            )
        }

        /** Report List screen ViewModel */
        viewModel {
            ReportListViewModel(
                application = androidApplication(),
                navigation = get(),
                playerRepository = get(),
                individualPlayerReportRepository = get(),
                deleteFirebaseUserInfo = get(),
                pdfGenerator = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Forgot Password screen ViewModel */
        viewModel {
            ForgotPasswordViewModel(
                application = androidApplication(),
                authenticationFirebase = get(),
                navigation = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Create Account screen ViewModel */
        viewModel {
            CreateAccountViewModel(
                navigation = get(),
                application = androidApplication(),
                createFirebaseUserInfo = get(),
                dataStorePreferencesWriter = get(),
                authenticationFirebase = get(),
                accountManager = get(),
                activeUserRepository = get(),
                declaredShotRepository = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Authentication screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            AuthenticationViewModel(
                savedStateHandle = stateHandle,
                readFirebaseUserInfo = get(),
                navigation = get(),
                application = androidApplication(),
                authenticationFirebase = get(),
                createFirebaseUserInfo = get(),
                activeUserRepository = get(),
                dataStorePreferencesWriter = get(),
                declaredShotRepository = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Settings screen ViewModel */
        viewModel {
            SettingsViewModel(
                navigation = get(),
                application = androidApplication(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                buildType = get(),
                activeUserRepository = get(),
                deleteFirebaseUserInfo = get(),
                accountManager = get(),
                authenticationFirebase = get(),
                firebaseAuth = get()

            )
        }

        /** Permission Education screen ViewModel */
        viewModel {
            PermissionEducationViewModel(
                navigation = get(),
                application = androidApplication()
            )
        }

        /** Onboarding Education screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            OnboardingEducationViewModel(
                savedStateHandle = stateHandle,
                navigation = get(),
                application = androidApplication()
            )
        }

        /** Terms and Conditions screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            TermsConditionsViewModel(
                savedStateHandle = stateHandle,
                navigation = get(),
                application = androidApplication(),
                dataStorePreferencesWriter = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Enabled Permissions screen ViewModel */
        viewModel {
            EnabledPermissionsViewModel(
                navigation = get(),
                application = androidApplication()
            )
        }

        /** Debug Toggle screen ViewModel */
        viewModel {
            DebugToggleViewModel(
                dataStorePreferencesReader = get(),
                dataStoreWriterPreferencesWriter = get(),
                navigation = get<DebugToggleNavigation>(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Declared Shots List screen ViewModel */
        viewModel {
            DeclaredShotsListViewModel(
                declaredShotRepository = get(),
                navigation = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Create/Edit Declared Shot screen ViewModel */
        viewModel { (stateHandle: SavedStateHandle) ->
            CreateEditDeclaredShotViewModel(
                savedStateHandle = stateHandle,
                application = androidApplication(),
                declaredShotRepository = get(),
                shotIgnoringRepository = get(),
                createFirebaseUserInfo = get(),
                updateFirebaseUserInfo = get(),
                deleteFirebaseUserInfo = get(),
                playerRepository = get(),
                navigation = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
            )
        }

        /** Account Info screen ViewModel */
        viewModel {
            AccountInfoViewModel(navigation = get())
        }

        /** Create Report screen ViewModel */
        viewModel {
            CreateReportViewModel(
                application = androidApplication(),
                navigation = get(),
                playerRepository = get(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                notifications = get(),
                pdfGenerator = get(),
                createFirebaseUserInfo = get(),
                individualPlayerReportRepository = get(),
                dateExt = get()
            )
        }

        /** Shots List screen ViewModel */
        viewModel {
            ShotsListViewModel(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
                navigation = get(),
                playerRepository = get(),
                dataStorePreferencesWriter = get(),
                dataStorePreferencesReader = get()
            )
        }
    }
}
