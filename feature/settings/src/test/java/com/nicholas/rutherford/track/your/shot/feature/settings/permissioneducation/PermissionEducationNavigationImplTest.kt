package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PermissionEducationNavigationImplTest {

    private lateinit var permissionEducationNavigationImpl: PermissionEducationNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        permissionEducationNavigationImpl = PermissionEducationNavigationImpl(navigator = navigator)
    }

    @Test
    fun `navigate to url action`() {
        val argumentCapture: CapturingSlot<String> = slot()
        val url = "url"

        permissionEducationNavigationImpl.navigateToUrl(url = url)

        verify { navigator.url(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = url

        Assertions.assertEquals(expectedAction, capturedArgument)
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        permissionEducationNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction = Constants.POP_DEFAULT_ACTION

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}
