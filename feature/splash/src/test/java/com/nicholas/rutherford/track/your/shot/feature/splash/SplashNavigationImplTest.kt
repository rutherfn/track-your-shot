package com.nicholas.rutherford.track.your.shot.feature.splash

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

class SplashNavigationImplTest {

    private lateinit var navigationImpl: SplashNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        navigationImpl = SplashNavigationImpl(navigator = navigator)
    }

    @Test
    fun `navigate to authentication`() {
        val username = "testUsername"
        val email = "testEmail"
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.navigateToAuthentication(username = username, email = email)

        verify { navigator.navigate(navigationAction = capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.SplashScreen.authentication(username = username, email = email)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to terms and conditions`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.navigateToTermsAndConditions()

        verify { navigator.navigate(navigationAction = capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.SplashScreen.termsConditions(shouldAcceptTerms = true)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)

    }

    @Test
    fun `navigate to players list`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.navigateToPlayersList()

        verify { navigator.navigate(navigationAction = capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.SplashScreen.playersList()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to login`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        navigationImpl.navigateToLogin()

        verify { navigator.navigate(navigationAction = capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.SplashScreen.login()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}