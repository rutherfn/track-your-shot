package com.nicholas.rutherford.track.your.shot

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.LiveHelp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toDisplayLabel
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
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.DeclaredShotState
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandViewModel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListViewModel

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [AppBarFactory] that provides pre-configured AppBar instances
 * for various screens throughout the app
 *
 * @param application The Application instance used to fetch string resources.
 */
class AppBarFactoryImpl(
    private val application: Application
) : AppBarFactory {

    /** Creates AppBar for the login screen with no icon. */
    override fun createLoginAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.trackYourShot,
            shouldShowIcon = false
        )

    /** Creates AppBar for the create account screen with back button. */
    override fun createAccountAppBar(viewModel: CreateAccountViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.createAccount,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    /** Creates AppBar for the forgot password screen with back button. */
    override fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.forgotPassword,
            onIconButtonClicked = { viewModel.onBackButtonClicked() }
        )

    /** Creates AppBar for the authentication/verify account screen with close icon. */
    override fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.verifyAccount,
            onIconButtonClicked = { viewModel.onNavigateClose() },
            imageVector = Icons.Filled.Close
        )

    /** Creates AppBar for the players list screen with menu and add player actions. */
    override fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.players,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { viewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { viewModel.onAddPlayerClicked() },
            shouldIncludeSpaceAfterDeclaration = false
        )

    /** Creates AppBar for the shots list screen, optionally showing all player shots or a specific player’s shots. */
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

    /** Creates AppBar for the report list screen with optional add report button. */
    override fun createReportListAppBar(params: ReportListParams): AppBar =
        AppBar(
            toolbarId = StringsIds.reports,
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )

    /** Creates AppBar for the settings screen with help action. */
    override fun createSettingsAppBar(params: SettingsParams): AppBar =
        AppBar(
            toolbarId = StringsIds.settings,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onHelpClicked.invoke() },
            secondaryImageVector = Icons.AutoMirrored.Filled.Help
        )

    /** Creates AppBar for the enabled permissions screen with back arrow. */
    override fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar =
        AppBar(
            toolbarId = StringsIds.enabledPermissions,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )

    /** Creates an AppBar for the debug toggle screen with back navigation. */
    override fun createDebugToggleAppBar(params: DebugToggleParams): AppBar =
        AppBar(
            toolbarId = StringsIds.debugToggles,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack
        )

    /** Creates AppBar for permission education screen. */
    override fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.moreInfo,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() }
        )

    /** Creates AppBar for onboarding education screen. */
    override fun createOnboardingEducationAppBar(
        viewModel: OnboardingEducationViewModel,
        isFirstTimeLaunched: Boolean
    ): AppBar =
        AppBar(
            toolbarId = StringsIds.usingTheApp,
            onIconButtonClicked = { viewModel.onGotItButtonClicked() },
            shouldShowIcon = !isFirstTimeLaunched
        )

    /** Creates AppBar for terms and conditions screen. */
    override fun createTermsAndConditionsAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.termsConditions,
            shouldShowIcon = false
        )

    /** Creates AppBar for the declared shots list screen with add declared shot button. */
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

    /** Creates AppBar for creating or editing a declared shot. */
    override fun createCreateEditDeclaredShotAppBar(
        declaredShotName: String,
        params: CreateEditDeclaredShotScreenParams
    ): AppBar {
        return AppBar(
            toolbarId = StringsIds.empty,
            toolbarTitle = declaredShotName.ifEmpty {
                application.getString(StringsIds.createShot)
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

    /** Creates AppBar for account info screen. */
    override fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.accountInfo,
            shouldShowMiddleContentAppBar = false,
            onIconButtonClicked = { viewModel.onToolbarMenuClicked() }
        )

    /** Creates AppBar for editing or creating a player. */
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

    /** Creates AppBar for logging a shot. */
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

    /** Creates AppBar for selecting a shot. */
    override fun createSelectShotAppBar(selectShotParams: SelectShotParams): AppBar =
        AppBar(
            toolbarId = StringsIds.selectAShot,
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = { selectShotParams.onBackButtonClicked.invoke() },
            onSecondaryIconButtonClicked = { selectShotParams.onHelpIconClicked.invoke() },
            secondaryIconTint = AppColors.White
        )

    /** Creates AppBar for creating a report screen. */
    override fun createReportScreenAppBar(params: CreateReportParams): AppBar =
        AppBar(
            toolbarId = StringsIds.createPlayerReport,
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }
        )

    /** Creates AppBar for voice command list screen. */
    override fun createVoiceCommandListScreenAppBar(voiceCommandListViewModel: VoiceCommandListViewModel): AppBar =
        AppBar(
            toolbarId = StringsIds.voiceCommands,
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { voiceCommandListViewModel.onToolbarMenuClicked() },
            onSecondaryIconButtonClicked = { },
            shouldIncludeSpaceAfterDeclaration = false,
            secondaryImageVector = Icons.AutoMirrored.Filled.LiveHelp
        )

    /** Creates AppBar for create edit voice command screen. */
    override fun createEditVoiceCommandCreateScreenAppBar(
        createEditVoiceCommandViewModel: CreateEditVoiceCommandViewModel,
        type: VoiceCommandTypes,
        isCreating: Boolean
    ): AppBar =
        AppBar(
            toolbarId = StringsIds.empty,
            toolbarTitle = if (isCreating) {
                application.getString(StringsIds.createXCommand, type.toDisplayLabel())
            } else {
                application.getString(StringsIds.editXCommand, type.toDisplayLabel())
            },
            shouldShowMiddleContentAppBar = false,
            onIconButtonClicked = { createEditVoiceCommandViewModel.onToolbarMenuClicked() }
        )

    /** Creates a default AppBar that is hidden. */
    override fun createDefaultAppBar(): AppBar =
        AppBar(
            toolbarId = StringsIds.empty,
            shouldShow = false
        )
}
