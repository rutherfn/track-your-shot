package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateAccountNavigationImplTest {

    private lateinit var createAccountNavigationImpl: CreateAccountNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        createAccountNavigationImpl = CreateAccountNavigationImpl(navigator = navigator)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        createAccountNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.LOGIN_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `navigate to authentication`() {
        val username = "username"
        val email = "email"

        val argumentCapture: CapturingSlot<NavigationAction> = slot()

        createAccountNavigationImpl.navigateToAuthentication(username = username, email = email)

        verify { navigator.navigate(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationActions.CreateAccountScreen.authentication(username = username, email = email)

        Assertions.assertEquals(expectedAction.destination, capturedArgument.destination)
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()

        createAccountNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `disable progress`() {
        createAccountNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        createAccountNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }
}
