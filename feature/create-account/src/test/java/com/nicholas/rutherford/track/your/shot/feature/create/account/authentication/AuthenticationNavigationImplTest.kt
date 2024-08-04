package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
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

class AuthenticationNavigationImplTest {

    private lateinit var authenticationNavigationImpl: AuthenticationNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        authenticationNavigationImpl = AuthenticationNavigationImpl(navigator = navigator)
    }

    @Test
    fun `finish action`() {
        authenticationNavigationImpl.finish()

        verify { navigator.finish(finishAction = true) }
    }

    @Test
    fun `open email action`() {
        authenticationNavigationImpl.openEmail()

        verify { navigator.emailAction(emailAction = true) }
    }

    @Test
    fun `navigate to login`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        authenticationNavigationImpl.navigateToLogin()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.AuthenticationScreen.login()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `navigate to players list`() {
        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        authenticationNavigationImpl.navigateToPlayersList()

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.AuthenticationScreen.playersList()

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `disable progress`() {
        authenticationNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()
        authenticationNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }
}
