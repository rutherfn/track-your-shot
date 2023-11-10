package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForgotPasswordNavigationImplTest {

    private lateinit var forgotPasswordNavigationImpl: ForgotPasswordNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        forgotPasswordNavigationImpl = ForgotPasswordNavigationImpl(navigator = navigator)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        forgotPasswordNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = NavigationDestinations.LOGIN_SCREEN

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `enable progress`() {
        val progress = Progress()

        forgotPasswordNavigationImpl.enableProgress(progress = progress)

        verify { navigator.progress(progressAction = progress) }
    }

    @Test
    fun `disable progress`() {
        forgotPasswordNavigationImpl.disableProgress()

        verify { navigator.progress(progressAction = null) }
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        forgotPasswordNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }
}
