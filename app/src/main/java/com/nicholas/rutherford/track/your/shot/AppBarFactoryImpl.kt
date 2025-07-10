package com.nicholas.rutherford.track.your.shot

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
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

    override fun createLoginAppBar(): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.trackYourShot,
            shouldShowIcon = false
        )

    override fun createCreateAccountAppBar(viewModel: CreateAccountViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.createAccount,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    override fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.forgotPassword,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    override fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.verifyAccount,
            onIconButtonClicked = { viewModel.onNavigateClose() },
            imageVector = Icons.Filled.Close
        )

    override fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.players,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { viewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { viewModel.onAddPlayerClicked() },
            shouldIncludeSpaceAfterDeclaration = false
        )

    override fun createShotsListAppBar(params: ShotsListScreenParams): AppBar2 =
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

    override fun createReportListAppBar(params: ReportListParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.reports,
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )

    override fun createSettingsAppBar(params: SettingsParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.settings,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onHelpClicked.invoke() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help
        )

    override fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.enabledPermissions,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )

    override fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.moreInfo,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() }
        )

    override fun createOnboardingEducationAppBar(
        viewModel: OnboardingEducationViewModel,
        isFirstTimeLaunched: Boolean
    ): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.usingTheApp,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() },
            shouldShowIcon = !isFirstTimeLaunched
        )

    override fun createTermsAndConditionsAppBar(): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.termsConditions,
            shouldShowIcon = false
        )

    override fun createDeclaredShotsListAppBar(params: DeclaredShotsListScreenParams): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.manageDeclaredShots,
            shouldShowMiddleContentAppBar = false,
            shouldShowSecondaryButton = true,
            secondaryIconTint = AppColors.White,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddDeclaredShotClicked.invoke() }
        )

    override fun createCreateEditDeclaredShotAppBar(params: CreateEditDeclaredShotScreenParams): AppBar2 {
        val declaredShotName = readSharedPreferences.declaredShotName()
        return AppBar2(
            toolbarId = StringsIds.empty,
            toolbarTitle = if (declaredShotName.isEmpty()) {
                application.getString(StringsIds.createShot)
            } else {
                "My Shots"
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

    override fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.accountInfo,
            shouldShowMiddleContentAppBar = false,
            onIconButtonClicked = {
                viewModel.onToolbarMenuClicked()
            }
        )

    override fun createDefaultAppBar(): AppBar2 =
        AppBar2(
            toolbarId = StringsIds.empty,
            shouldShow = false
        )
}
