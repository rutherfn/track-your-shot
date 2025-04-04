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
    val createEditDeclaredShotsViewModel: CreateEditDeclaredShotViewModel
)
