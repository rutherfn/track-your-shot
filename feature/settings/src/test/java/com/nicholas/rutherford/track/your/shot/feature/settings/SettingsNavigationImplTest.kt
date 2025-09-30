package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
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
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToDeclaredShotsList()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.declaredShotsList()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
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
        val expectedAction = NavigationActions.Settings.onboardingEducation(isFirstTimeLaunched = false)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to terms conditions`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToTermsConditions()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.termsConditions(shouldAcceptTerms = false)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to account info screen`() {
        mockkObject(UriEncoder)
        every { UriEncoder.encode(any()) } answers { firstArg() }

        val username = "testUser"
        val email = "test@email.com"
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToAccountInfoScreen(username = username, email = email)

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.accountInfo(username = username, email = email)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)

        unmockkObject(UriEncoder)
    }

    @Test
    fun `enable progress`() {
        val progress = Progress(title = "Loading...")

        settingsNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `disable progress`() {
        settingsNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun snackBar() {
        val snackBarInfo = SnackBarInfo(message = "Test message")

        settingsNavigationImpl.snackBar(snackBarInfo = snackBarInfo)

        verify { navigator.snackBar(snackBarInfo = snackBarInfo) }
    }

    @Test
    fun `navigate to debug toggles`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        settingsNavigationImpl.navigateToDebugToggles()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.Settings.debugToggle()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
