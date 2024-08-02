package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.mockk
import io.mockk.verify
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
    fun `open navigation drawer`() {
        settingsNavigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }
}
