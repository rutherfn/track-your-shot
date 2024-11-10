package com.nicholas.rutherford.track.your.shot.navigation

import androidx.navigation.NavOptions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions as Actions

class NavigationActionsTest {

    val testEmail = "testemail@yahoo.com"
    val testUsername = "testUsername112"

    @Nested
    inner class NavigationActions {

        @Nested
        inner class SplashScreen {

            @Test
            fun playersList() {
                Assertions.assertEquals(
                    Actions.SplashScreen.playersList().destination,
                    NavigationDestinations.PLAYERS_LIST_SCREEN
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.playersList().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )
            }

            @Test
            fun login() {
                Assertions.assertEquals(
                    Actions.SplashScreen.login().destination,
                    NavigationDestinations.LOGIN_SCREEN
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.login().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, false)
                        .build()
                )
            }

            @Test
            fun authentication() {
                Assertions.assertEquals(
                    Actions.SplashScreen.authentication(email = testEmail, username = testUsername).destination,
                    NavigationDestinationsWithParams.authenticationWithParams(username = testUsername, email = testEmail)
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.authentication(email = testEmail, username = testUsername).navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )
            }
        }

        @Nested inner class LoginScreen {

            @Test fun playersList() {
                Assertions.assertEquals(
                    Actions.LoginScreen.playersList().destination,
                    NavigationDestinations.PLAYERS_LIST_SCREEN
                )
                Assertions.assertEquals(
                    Actions.LoginScreen.playersList().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            @Test fun createAccount() {
                Assertions.assertEquals(
                    Actions.LoginScreen.createAccount().destination,
                    NavigationDestinations.CREATE_ACCOUNT_SCREEN
                )
                Assertions.assertEquals(
                    Actions.LoginScreen.createAccount().navOptions,
                    NavOptions.Builder()
                        .build()
                )
            }

            @Test fun forgot() {
                Assertions.assertEquals(
                    Actions.LoginScreen.forgotPassword().destination,
                    NavigationDestinations.FORGOT_PASSWORD_SCREEN
                )
                Assertions.assertEquals(
                    Actions.LoginScreen.forgotPassword().navOptions,
                    NavOptions.Builder()
                        .build()
                )
            }
        }

        @Nested
        inner class CreateAccountScreen {

            @Test
            fun authentication() {
                Assertions.assertEquals(
                    Actions.CreateAccountScreen.authentication(email = testEmail, username = testUsername).destination,
                    NavigationDestinationsWithParams.authenticationWithParams(username = testUsername, email = testEmail)
                )
                Assertions.assertEquals(
                    Actions.CreateAccountScreen.authentication(email = testEmail, username = testUsername).navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )
            }
        }

        @Nested
        inner class AuthenticationScreen {

            @Test
            fun playersList() {
                Assertions.assertEquals(
                    Actions.AuthenticationScreen.playersList().destination,
                    NavigationDestinations.PLAYERS_LIST_SCREEN
                )
                Assertions.assertEquals(
                    Actions.AuthenticationScreen.playersList().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
        }

        @Nested
        inner class DrawerScreen {

            @Test
            fun logout() {
                Assertions.assertEquals(
                    Actions.DrawerScreen.logout().destination,
                    NavigationDestinations.LOGIN_SCREEN
                )
                Assertions.assertEquals(
                    Actions.DrawerScreen.logout().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            @Test
            fun playersList() {
                Assertions.assertEquals(
                    Actions.DrawerScreen.playersList().destination,
                    NavigationDestinations.PLAYERS_LIST_SCREEN
                )
                Assertions.assertEquals(
                    Actions.DrawerScreen.playersList().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
        }

        @Nested
        inner class PlayersList {

            @Test
            fun createEditPlayer() {
                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayer().destination,
                    NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN
                )
                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayer().navOptions,
                    NavOptions.Builder().build()
                )
            }

            @Test
            fun createEditPlayerWithParams() {
                val firstName = "firstName"
                val lastName = "lastName"

                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayerWithParams(
                        firstName = firstName,
                        lastName = lastName
                    ).destination,
                    NavigationDestinationsWithParams.createEditPlayerWithParams(firstName = firstName, lastName = lastName)
                )
                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayerWithParams(
                        firstName = firstName,
                        lastName = lastName
                    ).navOptions,
                    NavOptions.Builder().build()
                )
            }
        }

        @Nested
        inner class CreateEditPlayer {

            @Test
            fun selectShot() {
                val isExistingPlayer = false
                val playerId = 2
                Assertions.assertEquals(
                    Actions.CreateEditPlayer.selectShot(isExistingPlayer = isExistingPlayer, playerId = playerId).destination,
                    NavigationDestinationsWithParams.selectShotWithParams(isExistingPlayer = isExistingPlayer, playerId = playerId)
                )
                Assertions.assertEquals(
                    Actions.CreateEditPlayer.selectShot(isExistingPlayer = isExistingPlayer, playerId = playerId).navOptions,
                    NavOptions.Builder().build()
                )
            }
        }

        @Nested
        inner class SelectShot {

            @Test
            fun logShot() {
                val isExistingPlayer = false
                val playerId = 2
                val shotType = 4
                val shotId = 2
                val viewCurrentExistingShot = false
                val viewCurrentPendingShot = false

                Assertions.assertEquals(
                    Actions.SelectShot.logShot(isExistingPlayer = isExistingPlayer, playerId = playerId, shotType = shotType, shotId = shotId, viewCurrentExistingShot = viewCurrentExistingShot, viewCurrentPendingShot = viewCurrentPendingShot).destination,
                    NavigationDestinationsWithParams.logShotWithParams(isExistingPlayer = isExistingPlayer, playerId = playerId, shotType = shotType, shotId = shotId, viewCurrentExistingShot = viewCurrentExistingShot, viewCurrentPendingShot = viewCurrentPendingShot)
                )
                Assertions.assertEquals(
                    Actions.SelectShot.logShot(isExistingPlayer = isExistingPlayer, playerId = playerId, shotType = shotType, shotId = shotId, viewCurrentExistingShot = viewCurrentExistingShot, viewCurrentPendingShot = viewCurrentExistingShot).navOptions,
                    NavOptions.Builder().build()
                )
            }
        }

        @Nested
        inner class LogShot {

            @Test
            fun createEditPlayer() {
                Assertions.assertEquals(
                    Actions.LogShot.createEditPlayer().destination,
                    NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN
                )
                Assertions.assertEquals(
                    Actions.LogShot.createEditPlayer().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                        .build()
                )
            }
        }

        @Nested
        inner class Settings {

            @Test
            fun permissionEducation() {
                Assertions.assertEquals(
                    Actions.Settings.permissionEducation().destination,
                    NavigationDestinations.PERMISSION_EDUCATION_SCREEN
                )
                Assertions.assertEquals(
                    Actions.Settings.permissionEducation().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                        .build()
                )
            }

            @Test
            fun termsConditions() {
                Assertions.assertEquals(
                    Actions.Settings.termsConditions().destination,
                    NavigationDestinations.TERMS_CONDITIONS_SCREEN
                )
                Assertions.assertEquals(
                    Actions.Settings.termsConditions().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                        .build()
                )
            }
        }
    }
}
