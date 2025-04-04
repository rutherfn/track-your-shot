package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SettingsNavigationImplTest {

    private lateinit var settingsNavigationImpl: SettingsNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        settingsNavigationImpl = SettingsNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "Title")

        settingsNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `navigate to declared shots list`() {
        settingsNavigationImpl.navigateToDeclaredShotsList()

        verify { navigator.navigate(navigationAction = NavigationActions.Settings.declaredShotsList()) }
    }

    @Test
    fun `open navigation drawer`() {
        settingsNavigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }

    @Test
    fun `navigate to enabled permissions`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToEnabledPermissions()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.enabledPermissions()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to permission education screen`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToPermissionEducationScreen()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.permissionEducation()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to onboarding education screen`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToOnboardingEducationScreen()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.onboardingEducation()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to terms conditions`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToTermsConditions()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.termsConditions(isAcknowledgeConditions = false)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
