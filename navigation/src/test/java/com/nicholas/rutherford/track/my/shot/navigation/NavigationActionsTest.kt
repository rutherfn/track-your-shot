package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions as Actions

class NavigationActionsTest {

    @Nested
    inner class NavigationActions {

        @Nested
        inner class SplashScreen {

            @Test
            fun home() {
                Assertions.assertEquals(
                    Actions.SplashScreen.home().destination,
                    NavigationDestinations.HOME_SCREEN
                )
                Assertions.assertEquals(
                    Actions.SplashScreen.home().navOptions,
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

            @Nested inner class Login {

                @Test fun home() {
                    Assertions.assertEquals(
                        Actions.LoginScreen.home().destination,
                        NavigationDestinations.HOME_SCREEN
                    )
                    Assertions.assertEquals(
                        Actions.LoginScreen.home().navOptions,
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
            inner class Home {

                @Test
                fun splash() {
                    Assertions.assertEquals(
                        Actions.HomeScreen.splash().destination,
                        NavigationDestinations.SPLASH_SCREEN
                    )
                }
            }
        }
    }
}