package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

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

class TermsConditionsNavigationImplTest {

    private lateinit var termsConditionsNavigationImpl: TermsConditionsNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        termsConditionsNavigationImpl = TermsConditionsNavigationImpl(navigator = navigator)
    }

    @Test
    fun `navigate to dev email`() {
        val argumentCapture: CapturingSlot<String> = slot()
        val email = "email"

        termsConditionsNavigationImpl.navigateToDevEmail(email = email)

        verify { navigator.emailDevAction(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = email

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `navigate to player list`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        termsConditionsNavigationImpl.navigateToPlayerList()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.TermsConditions.playerList()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to settings`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        termsConditionsNavigationImpl.navigateToSettings()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.TermsConditions.settings()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }
}
