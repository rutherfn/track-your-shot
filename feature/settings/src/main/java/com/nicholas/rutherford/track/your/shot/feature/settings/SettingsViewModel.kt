package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-27
 *
 * ViewModel responsible for managing and exposing the state and user interactions
 * for the Settings screen.
 *
 * This ViewModel handles:
 * - Settings screen state management (general, permission, and debug settings).
 * - Navigation to various settings sub-screens and external actions.
 * - Account management operations including account deletion.
 * - User authentication state management and logout operations.
 * - Alert and snackBar message display for user feedback.
 *
 * @param navigation Interface to handle navigation events and alerts.
 * @param application Application context used to access string resources.
 * @param scope Coroutine scope used to launch background operations.
 * @param buildType Represents the current build type (e.g., debug, release).
 * @param activeUserRepository Repository used to fetch the active user's data.
 * @param deleteFirebaseUserInfo Use case for deleting user information from Firebase Realtime Database.
 * @param accountManager AccountManager instance for managing user accounts and logout operations.
 * @param authenticationFirebase Handles Firebase authentication operations (account deletion).
 * @param firebaseAuth Firebase authentication instance for accessing current user.
 */
class SettingsViewModel(
    private val navigation: SettingsNavigation,
    private val application: Application,
    private val scope: CoroutineScope,
    private val buildType: BuildType,
    private val activeUserRepository: ActiveUserRepository,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val accountManager: AccountManager,
    private val authenticationFirebase: AuthenticationFirebase,
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    internal val settingsMutableStateFlow = MutableStateFlow(value = SettingsState())

    val settingsStateFlow = settingsMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    /**
     * Updates the settings state with general and permission settings labels.
     */
    private fun updateState() {
        settingsMutableStateFlow.update { settingsState ->
            settingsState.copy(
                generalSettings = generalSettings(),
                permissionSettings = permissionsSettings(),
                debugSettings = debugSettings()
            )
        }
    }

    /**
     * Returns a list of localized strings for general settings items.
     */
    internal fun generalSettings(): List<String> =
        listOf(
            application.getString(StringsIds.accountInfo),
            application.getString(StringsIds.manageDeclaredShots),
            application.getString(StringsIds.rateTheApp),
            application.getString(StringsIds.termsConditions),
            application.getString(StringsIds.usingTheApp)
        )

    /**
     * Returns a list of localized strings for permission settings items.
     */
    internal fun permissionsSettings(): List<String> =
        listOf(
            application.getString(StringsIds.enabledPermissions),
            application.getString(StringsIds.viewMoreInfo)
        )

    /**
     * Returns a list of localized strings for debug settings if build is debug.
     */
    internal fun debugSettings(): List<String> {
        return if (buildType.isDebug()) {
            listOf(
                application.getString(StringsIds.deleteAccount),
                application.getString(StringsIds.inAppFirebaseViewer),
                application.getString(StringsIds.toggles)
            )
        } else {
            emptyList()
        }
    }

    /**
     * Fetches the active user and navigates to the Account Info screen.
     */
    internal fun fetchActiveUserAndNavigateToAccountInfo() {
        scope.launch {
            val activeUser = activeUserRepository.fetchActiveUser()

            navigation.navigateToAccountInfoScreen(
                username = activeUser?.username ?: "",
                email = activeUser?.email ?: ""
            )
        }
    }

    /**
     * Handles the account deletion process when user confirms deletion.
     * * This function:
     * - Logs out the current user from the account manager.
     * - Combines Firebase authentication deletion and database cleanup operations.
     * - Provides appropriate feedback via snackBar messages based on operation results.
     * - Shows an alert if no current user exists.
     */
    suspend fun onDeleteAccountYesClicked() {
        firebaseAuth.currentUser?.let { currentUser ->
            accountManager.logout()

            combine(
                authenticationFirebase.attemptToDeleteCurrentUserFlow(currentUser = currentUser),
                deleteFirebaseUserInfo.deleteUser(uid = currentUser.uid)
            ) { authDeleted, dataDeleted ->
                Pair(authDeleted, dataDeleted)
            }.collectLatest { (authDeleted, dataDeleted) ->
                when {
                    authDeleted && dataDeleted -> navigation.snackBar(snackBarInfo = SnackBarInfo(message = "Account has been deleted."))
                    authDeleted && !dataDeleted -> navigation.snackBar(snackBarInfo = SnackBarInfo(message = "Account has been deleted, but realtime database record could not be deleted."))
                    !authDeleted && dataDeleted -> navigation.snackBar(snackBarInfo = SnackBarInfo(message = "Account has not been deleted, but realtime database record got deleted."))
                    else -> navigation.snackBar(snackBarInfo = SnackBarInfo(message = "No data was actually deleted"))
                }
            }
        } ?: navigation.alert(alert = userDoesNotExistAlert())
    }

    /**
     * Handles logic when a settings item is clicked, navigating to the appropriate screen.
     *
     * @param value The clicked item's label (localized string).
     */
    fun onSettingItemClicked(value: String) {
        when (value) {
            application.getString(StringsIds.usingTheApp) -> navigation.navigateToOnboardingEducationScreen()
            application.getString(StringsIds.termsConditions) -> navigation.navigateToTermsConditions()
            application.getString(StringsIds.manageDeclaredShots) -> navigation.navigateToDeclaredShotsList()
            application.getString(StringsIds.accountInfo) -> fetchActiveUserAndNavigateToAccountInfo()
            application.getString(StringsIds.enabledPermissions) -> navigation.navigateToEnabledPermissions()
            application.getString(StringsIds.viewMoreInfo) -> navigation.navigateToPermissionEducationScreen()
            application.getString(StringsIds.deleteAccount) -> navigation.alert(alert = deleteAccountAlert())
            application.getString(StringsIds.rateTheApp) -> navigation.requestReview()
            else -> navigation.navigateToDebugToggles()
        }
    }

    /**
     * Called when the toolbar menu icon is clicked.
     * Triggers navigation to open the drawer menu.
     */
    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    /**
     * Builds and returns the alert shown when the help icon is clicked in the settings screen.
     */
    fun settingsHelpAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.settings),
            description = application.getString(StringsIds.settingsHelpDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates and returns an alert dialog for confirming account deletion.
     * * @return Alert dialog with confirmation and dismissal options for account deletion.
     */
    fun deleteAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteAccount),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteYourAccount),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onDeleteAccountYesClicked() } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    /**
     * Creates and returns an alert dialog shown when account deletion fails due to missing user.
     * * @return Alert dialog informing the user that account deletion failed because the user doesn't exist.
     */
    fun userDoesNotExistAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.error),
            description = "We could not delete your account because, user does not exist.",
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Called when the help icon is clicked.
     * Triggers the display of the help alert dialog.
     */
    fun onHelpClicked() = navigation.alert(alert = settingsHelpAlert())
}
