package com.nicholas.rutherford.track.your.shot.navigation

import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions as Actions

class NavigationActionsTest {

    private val testEmail = "testemail@yahoo.com"
    private val testUsername = "testUsername112"

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
            fun termsConditions() {
                Assertions.assertEquals(
                    Actions.SplashScreen.termsConditions(shouldAcceptTerms = true).destination,
                    NavigationDestinationsWithParams.buildTermsConditionsDestination(
                        shouldAcceptTerms = true
                    )
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.termsConditions(shouldAcceptTerms = true).navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .setLaunchSingleTop(true)
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
                mockkObject(UriEncoder)

                every { UriEncoder.encode(any()) } answers { firstArg() }

                Assertions.assertEquals(
                    Actions.SplashScreen.authentication(
                        email = testEmail,
                        username = testUsername
                    ).destination,
                    NavigationDestinationsWithParams.buildAuthenticationDestination(
                        username = testUsername,
                        email = testEmail
                    )
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.authentication(
                        email = testEmail,
                        username = testUsername
                    ).navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )

                unmockkObject(UriEncoder)
            }
        }

        @Nested
        inner class LoginScreen {

            @Test
            fun playersList() {
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

            @Test
            fun createAccount() {
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

            @Test
            fun forgot() {
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
                mockkObject(UriEncoder)
                every { UriEncoder.encode(any()) } answers { firstArg() }

                Assertions.assertEquals(
                    Actions.CreateAccountScreen.authentication(
                        email = testEmail,
                        username = testUsername
                    ).destination,
                    NavigationDestinationsWithParams.authenticationWithParams(
                        username = testUsername,
                        email = testEmail
                    )
                )
                Assertions.assertEquals(
                    Actions.CreateAccountScreen.authentication(
                        email = testEmail,
                        username = testUsername
                    ).navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(0, true)
                        .build()
                )

                unmockkObject(UriEncoder)
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
            fun createEditPlayerWithParams() {
                val firstName = "firstName"
                val lastName = "lastName"

                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayerWithParams(
                        firstName = firstName,
                        lastName = lastName
                    ).destination,
                    "createEditPlayerScreen?firstName=firstName&lastName=lastName"
                )
                Assertions.assertEquals(
                    Actions.PlayersList.createEditPlayerWithParams(
                        firstName = firstName,
                        lastName = lastName
                    ).navOptions,
                    NavOptions.Builder().build()
                )
            }

            @Test
            fun shotList() {
                val shouldShowAllPlayerShots = true

                val result =
                    Actions.PlayersList.shotList(shouldShowAllPlayersShots = shouldShowAllPlayerShots)

                Assertions.assertEquals(
                    result.destination,
                    NavigationDestinationsWithParams.shotsListScreenWithParams(
                        shouldShowAllPlayersShots = shouldShowAllPlayerShots
                    )
                )
                Assertions.assertEquals(
                    result.navOptions,
                    NavOptions.Builder().build()
                )
            }
        }

        @Nested
        inner class ReportList {

            @Test
            fun createPlayerReport() {
                Assertions.assertEquals(
                    Actions.ReportList.createReport().destination,
                    "createReportScreen"
                )
                Assertions.assertEquals(
                    Actions.ReportList.createReport().navOptions,
                    NavOptions.Builder()
                        .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                        .build()
                )
            }
        }

        @Nested
        inner class DeclaredShotsList {

            @Test
            fun createEditDeclaredShot() {
                mockkObject(UriEncoder)

                every { UriEncoder.encode(any()) } answers { firstArg() }

                val shotName = "shotName"

                val result = Actions.DeclaredShotsList.createEditDeclaredShot(shotName = shotName)

                Assertions.assertEquals(
                    result.destination,
                    "createEditDeclaredShotsScreen?shotName=shotName"
                )
                Assertions.assertEquals(result.navOptions, NavOptions.Builder().build())

                unmockkObject(UriEncoder)
            }
        }

        @Nested
        inner class Settings {

            @Test
            fun accountInfo() {
                mockkObject(UriEncoder)
                every { UriEncoder.encode(any()) } answers { firstArg() }

                val username = "username"
                val email = "email"

                val result = Actions.Settings.accountInfo(username = username, email = email)

                Assertions.assertEquals(
                    result.destination,
                    NavigationDestinationsWithParams.accountInfoWithParams(
                        username = username,
                        email = email
                    )
                )
                Assertions.assertEquals(
                    result.navOptions,
                    NavOptions.Builder().build()
                )

                unmockkObject(UriEncoder)
            }

            @Test
            fun declaredShotsList() {
                val result = Actions.Settings.declaredShotsList()

                Assertions.assertEquals(
                    result.destination,
                    NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
                )
                Assertions.assertEquals(
                    result.navOptions,
                    NavOptions.Builder().build()
                )
            }

            @Test
            fun permissionEducation() {
                Assertions.assertEquals(
                    Actions.Settings.permissionEducation().destination,
                    NavigationDestinations.PERMISSION_EDUCATION_SCREEN
                )
            }
        }
    }
}
