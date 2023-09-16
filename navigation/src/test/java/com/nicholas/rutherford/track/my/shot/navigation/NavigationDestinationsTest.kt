package com.nicholas.rutherford.track.my.shot.navigation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NavigationDestinationsTest {

    lateinit var navigationDestinations: NavigationDestinations

    internal val authenticationScreen = "authenticationScreen"
    internal val createAccountScreen = "createAccountScreen"
    internal val forgotPasswordScreen = "forgotPasswordScreen"
    internal val homeScreen = "homeScreen"
    internal val playersListScreen = "playersListScreen"
    internal val loginScreen = "loginScreen"
    internal val splashScreen = "splashScreen"

    @BeforeEach
    fun beforeEach() {
        navigationDestinations = NavigationDestinations
    }

    @Nested
    inner class Constants {

        @Test fun `forgot password screen name should result in forgot screen`() {
            Assertions.assertEquals(navigationDestinations.FORGOT_PASSWORD_SCREEN, forgotPasswordScreen)
        }

        @Test
        fun `home screen name should result in home screen`() {
            Assertions.assertEquals(navigationDestinations.HOME_SCREEN, homeScreen)
        }

        @Test
        fun `players list name should result in players list screen`() {
            Assertions.assertEquals(navigationDestinations.PLAYERS_LIST_SCREEN, playersListScreen)
        }

        @Test fun `login screen name should result in login screen`() {
            Assertions.assertEquals(navigationDestinations.LOGIN_SCREEN, loginScreen)
        }

        @Test
        fun `splash screen name should result in splash screen`() {
            Assertions.assertEquals(navigationDestinations.SPLASH_SCREEN, splashScreen)
        }

        @Test
        fun `create account screen name should result in create account screen`() {
            Assertions.assertEquals(navigationDestinations.CREATE_ACCOUNT_SCREEN, createAccountScreen)
        }

        @Test
        fun `authetnication screen should result in authentication screen`() {
            Assertions.assertEquals(navigationDestinations.AUTHENTICATION_SCREEN, authenticationScreen)
        }
    }
}
