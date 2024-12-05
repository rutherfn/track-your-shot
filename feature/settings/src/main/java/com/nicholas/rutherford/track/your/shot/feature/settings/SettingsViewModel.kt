package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val navigation: SettingsNavigation,
    private val application: Application
) : ViewModel() {

    internal val settingsMutableStateFlow = MutableStateFlow(value = SettingsState())
    val settingsStateFlow = settingsMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    private fun updateState() {
        settingsMutableStateFlow.update { settingsState ->
            settingsState.copy(
                generalSettings = generalSettings(),
                permissionSettings = permissionsSettings()
            )
        }
    }

    internal fun generalSettings(): List<String> =
        listOf(
            application.getString(StringsIds.accountInfo),
            application.getString(StringsIds.termsConditions),
            application.getString(StringsIds.usingTheApp)
        )

    internal fun permissionsSettings(): List<String> =
        listOf(
            application.getString(StringsIds.enabledPermissions),
            application.getString(StringsIds.viewMoreInfo)
        )

    // todo add functionality to navigate to screens along with unit tests
    fun onSettingItemClicked(value: String) {
        when (value) {
            application.getString(StringsIds.usingTheApp) -> {
                navigation.navigateToOnboardingEducationScreen()
            }
            application.getString(StringsIds.termsConditions) -> {
                navigation.navigateToTermsConditions()
            }
            application.getString(StringsIds.accountInfo) -> {
                // navigate to account info page
            }
            application.getString(StringsIds.enabledPermissions) -> {
                navigation.navigateToEnabledPermissions()
            }
            else -> {
                navigation.navigateToPermissionEducationScreen()
            }
        }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun settingsHelpAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.settings),
            description = application.getString(StringsIds.settingsHelpDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )

        )
    }

    fun onHelpClicked() = navigation.alert(alert = settingsHelpAlert())
}
