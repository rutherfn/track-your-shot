package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and exposing the state and user interactions
 * for the Settings screen.
 *
 * @property navigation Used to navigate to different screens based on user interactions.
 * @property application Application context used to access string resources.
 * @property scope Coroutine scope used to launch background operations.
 * @property activeUserRepository Repository used to fetch the active user's data.
 */
class SettingsViewModel(
    private val navigation: SettingsNavigation,
    private val application: Application,
    private val scope: CoroutineScope,
    private val activeUserRepository: ActiveUserRepository
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
                permissionSettings = permissionsSettings()
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
            else -> navigation.navigateToPermissionEducationScreen()
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
     * Called when the help icon is clicked.
     * Triggers the display of the help alert dialog.
     */
    fun onHelpClicked() = navigation.alert(alert = settingsHelpAlert())
}

