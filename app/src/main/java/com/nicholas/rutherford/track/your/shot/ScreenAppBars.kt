package com.nicholas.rutherford.track.your.shot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Help
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListViewModel

object ScreenAppBars {

    fun buildCreateAccountAppBar(createAccountViewModel: CreateAccountViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.createAccount,
            onIconButtonClicked = { createAccountViewModel.onBackButtonClicked() }
        )

    fun buildForgotPasswordAppBar(forgotPasswordViewModel: ForgotPasswordViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.forgotPassword,
            onIconButtonClicked = { forgotPasswordViewModel.onBackButtonClicked() }
        )

    fun buildPlayersListAppBar(playersListViewModel: PlayersListViewModel): AppBar2 =
        AppBar2(
            toolbarId = R.string.players,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { playersListViewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { playersListViewModel.onAddPlayerClicked() },
            shouldIncludeSpaceAfterDeclaration = false
        )

    fun buildShotsListAppBar(params: ShotsListScreenParams): AppBar2 =
        AppBar2(
            toolbarId = if (params.shouldShowAllPlayerShots) {
                R.string.shots
            } else {
                R.string.player_shots
            },
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = {
                params.onHelpClicked()
//                    if (params.shouldShowAllPlayerShots) {
//                        // todo user should be taken where they can filter there shots screen
//                    }
            },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help,
            secondaryImageEnabled = true

        )

    fun buildReportListAppBar(params: ReportListParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.reports,
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )

    fun buildSettingsAppBar(params: SettingsParams): AppBar2 =
        AppBar2(
            toolbarId = R.string.settings,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                params.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                params.onHelpClicked.invoke()
            },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help
        )

    fun buildEnabledPermissionAppBar(params: EnabledPermissionsParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.enabledPermissions,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )


    fun buildDefaultAppBar(): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.empty,
            shouldShow = false
        )
}