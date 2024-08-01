package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.shouldAskForReadMediaImages
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

    fun updateState() {
        settingsMutableStateFlow.update { settingsState ->
            settingsState.copy(
                generalSettings = generalSettings(),
                permissionSettings = permissionsSettings()
            )
        }
    }

    internal fun generalSettings(): List<String> =
        listOf(
            application.getString(StringsIds.usingTheApp),
            application.getString(StringsIds.termsConditions),
            application.getString(StringsIds.accountInfo)
        )

    internal fun permissionsSettings(): List<String> =
        listOf(
            application.getString(StringsIds.camera),
            if (shouldAskForReadMediaImages()) {
                application.getString(StringsIds.readMediaStorage)
            } else {
                application.getString(StringsIds.readExternalStorage)
            }
        )

    // todo add functionality to navigate to screens along with unit tests
    fun onSettingItemClicked(value: String) {
        when (value) {
            application.getString(StringsIds.usingTheApp) -> {
                // navigate the onboarding screen
            }
            application.getString(StringsIds.termsConditions) -> {
                // navigate to terms and conditions page
            }
            else -> {
                // the only one left is account info so should navigate there
            }
        }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()
}