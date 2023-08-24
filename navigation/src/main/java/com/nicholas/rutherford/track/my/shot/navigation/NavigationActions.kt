package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions

object NavigationActions {

    object SplashScreen {
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        fun login() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, false)
                .build()
        }

        fun authentication(username: String, email: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.authenticationWithParams(username = username, email = email)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }
    }

    object LoginScreen {
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        fun createAccount() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_ACCOUNT_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
        fun forgotPassword() = object : NavigationAction {
            override val destination = NavigationDestinations.FORGOT_PASSWORD_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
    }

    object CreateAccountScreen {
        fun authentication(username: String, email: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.authenticationWithParams(username = username, email = email)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }
    }

    object AuthenticationScreen {

        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object HomeScreen {

        fun login() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, false)
                .build()
        }
    }
}
