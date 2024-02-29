package com.nicholas.rutherford.track.your.shot.navigation

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

    object DrawerScreen {

        fun logout() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object PlayersList {

        fun createEditPlayer() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        fun createEditPlayerWithParams(firstName: String, lastName: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.createEditPlayerWithParams(firstName = firstName, lastName = lastName)
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object CreateEditPlayer {

        fun selectShot(isExistingPlayer: Boolean, playerId: Int) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.selectShotWithParams(isExistingPlayer = isExistingPlayer, playerId = playerId)
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object SelectShot {
        fun logShot(
            isExistingPlayer: Boolean,
            playerId: Int,
            shotId: Int
        ) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.logShotWithParams(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId,
                shotId = shotId
            )
            override val navOptions = NavOptions.Builder().build()
        }
    }
}
