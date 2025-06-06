package com.nicholas.rutherford.track.your.shot.navigation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NavigationDestinationsTest {

    lateinit var navigationDestinations: NavigationDestinations

    internal val authenticationScreen = "authenticationScreen"
    internal val createAccountScreen = "createAccountScreen"
    internal val createEditPlayerScreen = "createEditPlayerScreen"
    internal val accountInfoScreen = "accountInfoScreen"
    internal val declaredShotsListScreen = "declaredShotsListScreen"
    internal val forgotPasswordScreen = "forgotPasswordScreen"
    internal val playersListScreen = "playersListScreen"
    internal val loginScreen = "loginScreen"
    internal val splashScreen = "splashScreen"
    internal val comparePlayersScreen = "comparePlayersScreen"
    internal val shotsListScreen = "shotsListScreen"
    internal val settingsScreen = "settingsScreen"
    internal val reportsListScreen = "reportsListScreen"
    internal val statsScreen = "statsScreen"
    internal val voiceCommandsScreen = "voiceCommandsScreen"
    internal val selectShotScreen = "selectShotScreen"
    internal val permissionEducationScreen = "permissionEducationScreen"
    internal var termsConditionsScreen = "termsConditionsScreen"

    @BeforeEach
    fun beforeEach() {
        navigationDestinations = NavigationDestinations
    }

    @Nested
    inner class Constants {

        @Test fun `voice commands screen should result in voice commands screen`() {
            Assertions.assertEquals(navigationDestinations.VOICE_COMMANDS_SCREEN, voiceCommandsScreen)
        }

        @Test fun `stats screen should result in stats screen`() {
            Assertions.assertEquals(navigationDestinations.STATS_SCREEN, statsScreen)
        }

        @Test fun `settings screen should result in settings screen`() {
            Assertions.assertEquals(navigationDestinations.SETTINGS_SCREEN, settingsScreen)
        }

        @Test fun `reports list should result in reports list screen`() {
            Assertions.assertEquals(navigationDestinations.REPORTS_LIST_SCREEN, reportsListScreen)
        }

        @Test fun `compare players screen should result in compare players screen`() {
            Assertions.assertEquals(navigationDestinations.COMPARE_PLAYERS_SCREEN, comparePlayersScreen)
        }

        @Test fun `shots list screen should result in shots list screen`() {
            Assertions.assertEquals(navigationDestinations.SHOTS_LIST_SCREEN, shotsListScreen)
        }

        @Test fun `forgot password screen name should result in forgot screen`() {
            Assertions.assertEquals(navigationDestinations.FORGOT_PASSWORD_SCREEN, forgotPasswordScreen)
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
        fun `create edit player screen name should result in create edit player screen`() {
            Assertions.assertEquals(navigationDestinations.CREATE_EDIT_PLAYER_SCREEN, createEditPlayerScreen)
        }

        @Test
        fun `account info screen name should result in account info screen`() {
            Assertions.assertEquals(navigationDestinations.ACCOUNT_INFO_SCREEN, accountInfoScreen)
        }

        @Test
        fun `declared shots list screen should result in declared shots list`() {
            Assertions.assertEquals(navigationDestinations.DECLARED_SHOTS_LIST_SCREEN, declaredShotsListScreen)
        }

        @Test
        fun `authentication screen should result in authentication screen`() {
            Assertions.assertEquals(navigationDestinations.AUTHENTICATION_SCREEN, authenticationScreen)
        }

        @Test
        fun `select shot screen should result in select a shot screen`() {
            Assertions.assertEquals(navigationDestinations.SELECT_SHOT_SCREEN, selectShotScreen)
        }

        @Test
        fun `permission education screen should result in permission education screen`() {
            Assertions.assertEquals(navigationDestinations.PERMISSION_EDUCATION_SCREEN, permissionEducationScreen)
        }

        @Test
        fun `terms conditions screen should result in terms conditions screen`() {
            Assertions.assertEquals(navigationDestinations.TERMS_CONDITIONS_SCREEN, termsConditionsScreen)
        }
    }
}
