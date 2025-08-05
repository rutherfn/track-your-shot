package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.compose.components.AppBar
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotParams
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportParams
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams

interface AppBarFactory {
    fun createLoginAppBar(): AppBar
    fun createAccountAppBar(viewModel: CreateAccountViewModel): AppBar
    fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar
    fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar
    fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar
    fun createShotsListAppBar(params: ShotsListScreenParams): AppBar
    fun createReportListAppBar(params: ReportListParams): AppBar
    fun createSettingsAppBar(params: SettingsParams): AppBar
    fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar
    fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar
    fun createOnboardingEducationAppBar(viewModel: OnboardingEducationViewModel, isFirstTimeLaunched: Boolean): AppBar
    fun createTermsAndConditionsAppBar(): AppBar
    fun createDeclaredShotsListAppBar(params: DeclaredShotsListScreenParams): AppBar
    fun createCreateEditDeclaredShotAppBar(params: CreateEditDeclaredShotScreenParams): AppBar
    fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar
    fun createEditPlayerAppBar(params: CreateEditPlayerParams, isEditable: Boolean): AppBar
    fun createLogShotAppBar(params: LogShotParams): AppBar
    fun createSelectShotAppBar(selectShotParams: SelectShotParams): AppBar
    fun createReportScreenAppBar(params: CreateReportParams): AppBar
    fun createDefaultAppBar(): AppBar
}
