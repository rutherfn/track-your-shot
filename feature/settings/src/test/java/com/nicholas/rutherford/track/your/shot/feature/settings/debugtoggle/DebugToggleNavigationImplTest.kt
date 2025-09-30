package com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DebugToggleNavigationImplTest {

    private lateinit var debugToggleNavigationImpl: DebugToggleNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        debugToggleNavigationImpl = DebugToggleNavigationImpl(navigator)
    }

    @Test
    fun pop() {
        debugToggleNavigationImpl.pop()

        verify { navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION) }
    }
}
