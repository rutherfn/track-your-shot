package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SettingsViewModelTest {

    private lateinit var settingsViewModel: SettingsViewModel

    private var navigation = mockk<SettingsNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.accountInfo) } returns "Account Info"
        every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
        every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
        every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
        every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"

        settingsViewModel = SettingsViewModel(navigation = navigation, application = application)
    }

    @Test
    fun `init should initial state`() {
        Assertions.assertEquals(
            settingsViewModel.settingsMutableStateFlow.value,
            SettingsState(
                generalSettings = listOf("Account Info", "Terms & Conditions", "Using The App"),
                permissionSettings = listOf("Enabled Permissions", "View More Info")
            )
        )
    }

    @Test
    fun `general settings should return expected list`() {
        Assertions.assertEquals(
            settingsViewModel.generalSettings(),
            listOf("Account Info", "Terms & Conditions", "Using The App")
        )
    }

    @Test
    fun `permission settings should return expected list`() {
        Assertions.assertEquals(
            settingsViewModel.permissionsSettings(),
            listOf("Enabled Permissions", "View More Info")
        )
    }

    @Test
    fun `on toolbar menu clicked`() {
        settingsViewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }
}