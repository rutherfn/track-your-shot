package com.nicholas.rutherford.track.my.shot.feature.login

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginNavigationImplTest {

    private lateinit var loginNavigationImpl: LoginNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        loginNavigationImpl = LoginNavigationImpl(navigator = navigator)
    }

    @Test
    fun `navigate to forgot password`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        loginNavigationImpl.navigateToForgotPassword()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.LoginScreen.forgotPassword()

        assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to create account`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        loginNavigationImpl.navigateToCreateAccount()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.LoginScreen.createAccount()

        assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to players list`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        loginNavigationImpl.navigateToPlayersList()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.LoginScreen.playersList()

        assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()

        loginNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `disable progress`() {
        loginNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        loginNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }
}
