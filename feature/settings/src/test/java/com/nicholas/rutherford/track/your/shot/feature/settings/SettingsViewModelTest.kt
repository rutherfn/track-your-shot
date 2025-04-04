package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SettingsViewModelTest {

    private lateinit var settingsViewModel: SettingsViewModel

    private var navigation = mockk<SettingsNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.manageDeclaredShots) } returns "Manage Declared Shots"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.settings) } returns "Settings"
        every { application.getString(StringsIds.accountInfo) } returns "Account Info"
        every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
        every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
        every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
        every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"
        every { application.getString(StringsIds.settingsHelpDescription) } returns "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them."

        settingsViewModel = SettingsViewModel(
            navigation = navigation,
            application = application,
            scope = scope,
            activeUserRepository = activeUserRepository
        )
    }

    @Test
    fun `init should initial state`() {
        Assertions.assertEquals(
            settingsViewModel.settingsMutableStateFlow.value,
            SettingsState(
                generalSettings = listOf("Account Info", "Manage Declared Shots", "Terms & Conditions", "Using The App"),
                permissionSettings = listOf("Enabled Permissions", "View More Info")
            )
        )
    }

    @Test
    fun `general settings should return expected list`() {
        Assertions.assertEquals(
            settingsViewModel.generalSettings(),
            listOf("Account Info", "Manage Declared Shots", "Terms & Conditions", "Using The App")
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

    @Test
    fun `on settings help alert`() {
        val alert = settingsViewModel.settingsHelpAlert()

        Assertions.assertEquals(
            alert,
            Alert(
                title = "Settings",
                description = "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them.",
                confirmButton = AlertConfirmAndDismissButton(buttonText = "Got It")
            )
        )
    }

    @Test
    fun `on help clicked`() {
        settingsViewModel.onHelpClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class OnSettingItemClicked {

        @Test
        fun `when value passed in is using the app should navigate to onboarding education screen`() {
            settingsViewModel.onSettingItemClicked(value = "Using The App")

            verify { navigation.navigateToOnboardingEducationScreen() }
        }

        @Test
        fun `when value passed in is terms and conditions should navigate to terms and conditions screen`() {
            settingsViewModel.onSettingItemClicked(value = "Terms & Conditions")

            verify { navigation.navigateToTermsConditions() }
        }

        @Test
        fun `when value passed in is account info should navigate to account info screen`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns ActiveUser(
                id = 1,
                accountHasBeenCreated = false,
                username = "username",
                email = "email",
                firebaseAccountInfoKey = "key"
            )

            settingsViewModel.onSettingItemClicked(value = "Account Info")

            verify { navigation.navigateToAccountInfoScreen(username = "username", email = "email") }
        }

        @Test
        fun `when value passed in is enabled permissions should navigate to enabled permissions screen`() {
            settingsViewModel.onSettingItemClicked(value = "Enabled Permissions")

            verify { navigation.navigateToEnabledPermissions() }
        }

        @Test
        fun `when value passed in does not meet any conditions should navigate to permission education screen`() {
            settingsViewModel.onSettingItemClicked(value = "View Mode Info")

            verify { navigation.navigateToPermissionEducationScreen() }
        }
    }
}
