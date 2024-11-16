package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.CapturingSlot
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EnabledPermissionsNavigationImplTest {

    private lateinit var enabledPermissionsNavigationImpl: EnabledPermissionsNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        enabledPermissionsNavigationImpl = EnabledPermissionsNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "title")

        enabledPermissionsNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `app settings action`() {
        enabledPermissionsNavigationImpl.appSettings()

        verify { navigator.appSettings(appSettingsAction = true) }
    }

    @Test
    fun `pop action`() {
        val argumentCapture: CapturingSlot<String> = slot()

        enabledPermissionsNavigationImpl.pop()

        verify { navigator.pop(capture(argumentCapture)) }

        val capturedArgument = argumentCapture.captured
        val expectedAction =  Constants.POP_DEFAULT_ACTION

        Assertions.assertEquals(expectedAction, capturedArgument)
    }
}
