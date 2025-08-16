package com.nicholas.rutherford.track.your.shot

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
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
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.DeclaredShotState
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences

class AppBarFactoryImpl(
    private val readSharedPreferences: ReadSharedPreferences,
    private val application: Application
) : AppBarFactory {

    override fun createLoginAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.trackYourShot,
            shouldShowIcon = false
        )

    override fun createAccountAppBar(viewModel: CreateAccountViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.createAccount,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    override fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.forgotPassword,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    override fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.verifyAccount,
            onIconButtonClicked = { viewModel.onNavigateClose() },
            imageVector = Icons.Filled.Close
        )

    override fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.players,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { viewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { viewModel.onAddPlayerClicked() },
            shouldIncludeSpaceAfterDeclaration = false
        )

    override fun createShotsListAppBar(params: ShotsListScreenParams): AppBar =
        AppBar(
            toolbarId = if (params.shouldShowAllPlayerShots) {
                StringsIds.shots
            } else {
                StringsIds.playerShots
            },
            imageVector = if (params.shouldShowAllPlayerShots) {
                null
            } else {
                Icons.AutoMirrored.Filled.ArrowBack
            },
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { params.onHelpClicked() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help,
            secondaryImageEnabled = true
        )

    override fun createReportListAppBar(params: ReportListParams): AppBar =
        AppBar(
            toolbarId = StringsIds.reports,
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )

    override fun createSettingsAppBar(params: SettingsParams): AppBar =
        AppBar(
            toolbarId = StringsIds.settings,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onHelpClicked.invoke() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help
        )

    override fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar =
        AppBar(
            toolbarId = StringsIds.enabledPermissions,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )

    override fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.moreInfo,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() }
        )

    override fun createOnboardingEducationAppBar(
        viewModel: OnboardingEducationViewModel,
        isFirstTimeLaunched: Boolean
    ): AppBar =
        AppBar(
            toolbarId = StringsIds.usingTheApp,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() },
            shouldShowIcon = !isFirstTimeLaunched
        )

    override fun createTermsAndConditionsAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.termsConditions,
            shouldShowIcon = false
        )

    override fun createDeclaredShotsListAppBar(params: DeclaredShotsListScreenParams): AppBar =
        AppBar(
            toolbarId = StringsIds.manageDeclaredShots,
            shouldShowMiddleContentAppBar = false,
            shouldShowSecondaryButton = true,
            secondaryIconTint = AppColors.White,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddDeclaredShotClicked.invoke() },
            secondaryImageVector = Icons.Default.Add
        )

    override fun createCreateEditDeclaredShotAppBar(declaredShotName: String, params: CreateEditDeclaredShotScreenParams): AppBar {
        return AppBar(
            toolbarId = StringsIds.empty,
            toolbarTitle = if (declaredShotName.isEmpty()) {
                application.getString(StringsIds.createShot)
            } else {
                declaredShotName
            },
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            shouldShowSecondaryButton = true,
            secondaryIconTint = AppColors.White,
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
    }

    override fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.accountInfo,
            shouldShowMiddleContentAppBar = false,
            onIconButtonClicked = {
                viewModel.onToolbarMenuClicked()
            }
        )

    override fun createEditPlayerAppBar(
        params: CreateEditPlayerParams,
        isEditable: Boolean
    ): AppBar =
        AppBar(
            toolbarId = if (isEditable) {
                StringsIds.editPlayer
            } else {
                StringsIds.createPlayer
            },
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onCreatePlayerClicked.invoke() },
            secondaryIconTint = AppColors.White
        )

    override fun createLogShotAppBar(params: LogShotParams): AppBar =
        AppBar(
            toolbarId = StringsIds.logShot,
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = { params.onBackButtonClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onSaveClicked.invoke() },
            secondaryIconTint = AppColors.White
        )

    override fun createSelectShotAppBar(selectShotParams: SelectShotParams): AppBar =
        AppBar(
            toolbarId = StringsIds.selectAShot,
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = {
                selectShotParams.onBackButtonClicked.invoke()
            },
            onSecondaryIconButtonClicked = { selectShotParams.onHelpIconClicked.invoke() },
            secondaryIconTint = AppColors.White
        )

    override fun createReportScreenAppBar(params: CreateReportParams): AppBar =
        AppBar(
            toolbarId = StringsIds.createPlayerReport,
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }

        )

    override fun createDefaultAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.empty,
            shouldShow = false
        )
}
