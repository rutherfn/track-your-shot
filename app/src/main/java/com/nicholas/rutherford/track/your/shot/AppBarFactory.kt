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
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Factory interface responsible for providing pre-configured [AppBar] instances
 * for various screens in the Track Your Shot application.
 *
 * Implementations should return AppBar instances customized with appropriate
 * titles, icons, and actions for each screen type.
 */
interface AppBarFactory {

    /** Creates an AppBar for the login screen. */
    fun createLoginAppBar(): AppBar

    /** Creates an AppBar for the create account screen with back navigation. */
    fun createAccountAppBar(viewModel: CreateAccountViewModel): AppBar

    /** Creates an AppBar for the forgot password screen with back navigation. */
    fun createForgotPasswordAppBar(viewModel: ForgotPasswordViewModel): AppBar

    /** Creates an AppBar for the authentication/verify account screen with close action. */
    fun createAuthenticationAppBar(viewModel: AuthenticationViewModel): AppBar

    /** Creates an AppBar for the players list screen with menu and add player actions. */
    fun createPlayersListAppBar(viewModel: PlayersListViewModel): AppBar

    /** Creates an AppBar for the shots list screen. Adjusts title and icon based on whether all player shots are shown. */
    fun createShotsListAppBar(params: ShotsListScreenParams): AppBar

    /** Creates an AppBar for the report list screen, with optional add report action. */
    fun createReportListAppBar(params: ReportListParams): AppBar

    /** Creates an AppBar for the settings screen with help action. */
    fun createSettingsAppBar(params: SettingsParams): AppBar

    /** Creates an AppBar for the enabled permissions screen with back navigation. */
    fun createEnabledPermissionsAppBar(params: EnabledPermissionsParams): AppBar

    /** Creates an AppBar for the debug toggle screen with back navigation. */
    fun createDebugToggleAppBar(params: DebugToggleParams): AppBar

    /** Creates an AppBar for the permission education screen. */
    fun createPermissionEducationAppBar(viewModel: PermissionEducationViewModel): AppBar

    /** Creates an AppBar for onboarding education screen.
     * @param isFirstTimeLaunched Determines whether the main icon should be displayed.
     */
    fun createOnboardingEducationAppBar(viewModel: OnboardingEducationViewModel, isFirstTimeLaunched: Boolean): AppBar

    /** Creates an AppBar for the terms and conditions screen. */
    fun createTermsAndConditionsAppBar(): AppBar

    /** Creates an AppBar for the declared shots list screen with add action. */
    fun createDeclaredShotsListAppBar(params: DeclaredShotsListScreenParams): AppBar

    /** Creates an AppBar for creating or editing a declared shot.
     * @param declaredShotName Name of the declared shot to show as title.
     */
    fun createCreateEditDeclaredShotAppBar(declaredShotName: String, params: CreateEditDeclaredShotScreenParams): AppBar

    /** Creates an AppBar for the account info screen. */
    fun createAccountInfoAppBar(viewModel: AccountInfoViewModel): AppBar

    /** Creates an AppBar for editing or creating a player.
     * @param isEditable Indicates if the player is being edited (true) or created (false).
     */
    fun createEditPlayerAppBar(params: CreateEditPlayerParams, isEditable: Boolean): AppBar

    /** Creates an AppBar for logging a shot. */
    fun createLogShotAppBar(params: LogShotParams): AppBar

    /** Creates an AppBar for selecting a shot. */
    fun createSelectShotAppBar(selectShotParams: SelectShotParams): AppBar

    /** Creates an AppBar for creating a report screen. */
    fun createReportScreenAppBar(params: CreateReportParams): AppBar

    /** Creates a default, hidden AppBar. */
    fun createDefaultAppBar(): AppBar
}
