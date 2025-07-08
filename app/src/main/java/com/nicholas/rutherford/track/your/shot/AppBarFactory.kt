package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
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
    fun createLoginAppBar(): AppBar2
    fun createCreateAccountAppBar(viewModel: CreateAccountViewModel): AppBar2
    fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar2
    fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar2
    fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar2
    fun createShotsListAppBar(params: ShotsListScreenParams): AppBar2
    fun createReportListAppBar(params: ReportListParams): AppBar2
    fun createSettingsAppBar(params: SettingsParams): AppBar2
    fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar2
    fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar2
    fun createOnboardingEducationAppBar(viewModel: OnboardingEducationViewModel): AppBar2
    fun createTermsAndConditionsAppBar(): AppBar2
    fun createDeclaredShotsListAppBar(params: DeclaredShotsListScreenParams): AppBar2
    fun createCreateEditDeclaredShotAppBar(params: CreateEditDeclaredShotScreenParams): AppBar2
    fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar2
    fun createDefaultAppBar(): AppBar2
}

