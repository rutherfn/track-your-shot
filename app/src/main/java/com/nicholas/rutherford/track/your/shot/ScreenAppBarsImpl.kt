package com.nicholas.rutherford.track.your.shot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams

class ScreenAppBarsImpl : ScreenAppBars {

    override fun buildCreateAccountAppBar(createAccountViewModel: CreateAccountViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.createAccount,
            onIconButtonClicked = { createAccountViewModel.onBackButtonClicked() }
        )

    override fun buildForgotPasswordAppBar(forgotPasswordViewModel: ForgotPasswordViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.forgotPassword,
            onIconButtonClicked = { forgotPasswordViewModel.onBackButtonClicked() }
        )

    override fun buildPlayersListAppBar(playersListViewModel: PlayersListViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.players,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { playersListViewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { playersListViewModel.onAddPlayerClicked() },
            shouldIncludeSpaceAfterDeclaration = false
        )

    override fun buildShotsListAppBar(params: ShotsListScreenParams): AppBar2 =
        AppBar2(
            toolbarId = if (params.shouldShowAllPlayerShots) {
                StringsIds.shots
            } else {
                StringsIds.playerShots
            },
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { params.onHelpClicked() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help,
            secondaryImageEnabled = true
        )

    override fun buildReportListAppBar(params: ReportListParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.reports,
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )

    override fun buildSettingsAppBar(params: SettingsParams): AppBar2 =
        AppBar2(
            toolbarId = R.string.settings,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onHelpClicked.invoke() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help
        )

    override fun buildEnabledPermissionAppBar(params: EnabledPermissionsParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.enabledPermissions,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )

    override fun buildPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar2 =
        AppBar2(
            toolbarId = R.string.more_info,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() }
        )

    override fun buildOnboardingEducationAppBar(viewModel: OnboardingEducationViewModel): AppBar2 =
        AppBar2(
            toolbarId = R.string.using_the_app,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() }
        )

    override fun buildTermsAndConditionsAppBar(): AppBar2 =
        AppBar2(
            toolbarId = R.string.terms_conditions,
            shouldShowIcon = false
        )

    override fun buildDeclaredShotListAppBar(params: DeclaredShotsListScreenParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.manageDeclaredShots,
            shouldShowMiddleContentAppBar = false,
            shouldShowSecondaryButton = true,
            secondaryIconTint = AppColors.White,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddDeclaredShotClicked.invoke() }
        )

    override fun buildCreateEditDeclaredShotAppBar(params: CreateEditDeclaredShotScreenParams): AppBar2 =
        AppBar2(
            toolbarId = R.string.empty,
            toolbarTitle = params.state.toolbarTitle,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            shouldShowSecondaryButton = true,
            onSecondaryIconButtonClicked = {
                if (params.state.declaredShotState == DeclaredShotState.VIEWING) {
                    params.onEditShotPencilClicked.invoke()
                } else {
                    params.onEditOrCreateNewShot.invoke()
                }
            },
            secondaryImageEnabled = true,
            secondaryImageVector = if (params.state.declaredShotState == DeclaredShotState.VIEWING) {
                Icons.Default.Edit
            } else {
                Icons.Default.Save
            }
        )

    override fun buildDefaultAppBar(): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.empty,
            shouldShow = false
        )
}
