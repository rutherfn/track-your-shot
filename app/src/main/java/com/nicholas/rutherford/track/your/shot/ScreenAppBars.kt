package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams

interface ScreenAppBars {

    fun buildCreateAccountAppBar(createAccountViewModel: CreateAccountViewModel): AppBar2

    fun buildForgotPasswordAppBar(forgotPasswordViewModel: ForgotPasswordViewModel): AppBar2

    fun buildPlayersListAppBar(playersListViewModel: PlayersListViewModel): AppBar2

    fun buildShotsListAppBar(params: ShotsListScreenParams): AppBar2

    fun buildReportListAppBar(params: ReportListParams): AppBar2

    fun buildSettingsAppBar(params: SettingsParams): AppBar2

    fun buildEnabledPermissionAppBar(params: EnabledPermissionsParams): AppBar2

    fun buildPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar2

    fun buildOnboardingEducationAppBar(viewModel: OnboardingEducationViewModel): AppBar2

    fun buildTermsAndConditionsAppBar(): AppBar2

    fun buildDeclaredShotListAppBar(params: DeclaredShotsListScreenParams): AppBar2

    fun buildCreateEditDeclaredShotAppBar(params: CreateEditDeclaredShotScreenParams): AppBar2

    fun buildDefaultAppBar(): AppBar2
}
