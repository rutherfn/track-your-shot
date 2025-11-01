package com.nicholas.rutherford.track.your.shot

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
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandViewModel

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A centralized data class that holds all ViewModel instances used across the app.
 *
 * This class simplifies dependency injection or passing multiple ViewModels together,
 * especially when multiple screens or features require access to several ViewModels.
 *
 * Todo -> Get rid of it, we are keeping it around for [NavigationComponent] but if we refactor that code,
 * don't think we will need to use it.
 * Each property represents a single feature's ViewModel:
 * - [mainActivityViewModel] manages main activity logic and global state.
 * - [splashViewModel] handles logic for the splash screen.
 * - [loginViewModel] manages login screen state and interactions.
 * - [playersListViewModel] handles player list screen data and actions.
 * - [createEditPlayerViewModel] manages create/edit player screen state.
 * - [forgotPasswordViewModel] handles forgot password flow.
 * - [createAccountViewModel] manages create account screen state.
 * - [authenticationViewModel] handles authentication logic.
 * - [selectShotViewModel] manages shot selection screen.
 * - [logShotViewModel] handles logging shots and related state.
 * - [settingsViewModel] manages settings screen state.
 * - [permissionEducationViewModel] handles permission education flow.
 * - [termsConditionsViewModel] manages terms and conditions screen state.
 * - [onboardingEducationViewModel] handles onboarding education flow.
 * - [enabledPermissionsViewModel] tracks enabled permissions settings.
 * - [accountInfoViewModel] manages account info screen state.
 * - [reportListViewModel] handles reports list screen state.
 * - [createReportViewModel] manages creating a report.
 * - [shotsListViewModel] handles shots list screen state.
 * - [declaredShotsListViewModel] manages declared shots list screen state.
 * - [createEditDeclaredShotsViewModel] handles create/edit declared shots flow.
 */
data class ViewModels(
    val mainActivityViewModel: MainActivityViewModel,
    val splashViewModel: SplashViewModel,
    val loginViewModel: LoginViewModel,
    val playersListViewModel: PlayersListViewModel,
    val createEditPlayerViewModel: CreateEditPlayerViewModel,
    val forgotPasswordViewModel: ForgotPasswordViewModel,
    val createAccountViewModel: CreateAccountViewModel,
    val authenticationViewModel: AuthenticationViewModel,
    val selectShotViewModel: SelectShotViewModel,
    val logShotViewModel: LogShotViewModel,
    val settingsViewModel: SettingsViewModel,
    val permissionEducationViewModel: PermissionEducationViewModel,
    val termsConditionsViewModel: TermsConditionsViewModel,
    val onboardingEducationViewModel: OnboardingEducationViewModel,
    val enabledPermissionsViewModel: EnabledPermissionsViewModel,
    val accountInfoViewModel: AccountInfoViewModel,
    val reportListViewModel: ReportListViewModel,
    val createReportViewModel: CreateReportViewModel,
    val shotsListViewModel: ShotsListViewModel,
    val declaredShotsListViewModel: DeclaredShotsListViewModel,
    val createEditDeclaredShotsViewModel: CreateEditDeclaredShotViewModel,
    val createEditVoiceCommandViewModel: CreateEditVoiceCommandViewModel
)
