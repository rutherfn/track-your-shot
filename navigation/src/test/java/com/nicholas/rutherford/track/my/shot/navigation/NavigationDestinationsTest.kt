package com.nicholas.rutherford.track.my.shot.navigation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NavigationDestinationsTest {

    lateinit var navigationDestinations: NavigationDestinations

    internal val splashScreen = "splashScreen"
    internal val homeScreen = "homeScreen"

    @BeforeEach
    fun beforeEach() {
        navigationDestinations = NavigationDestinations
    }

    @Nested
    inner class Constants {

        @Test
        fun `splash screen name should result in splash screen`() {
            Assertions.assertEquals(navigationDestinations.SPLASH_SCREEN, splashScreen)
        }

        @Test
        fun `home screen name should result in home screen`() {
            Assertions.assertEquals(navigationDestinations.HOME_SCREEN, homeScreen)
        }
    }
}
