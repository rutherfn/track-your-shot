package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions

object NavigationActions {

    object SplashScreen {
        fun home() = object : NavigationAction {
            override val destination = NavigationDestinations.HOME_SCREEN
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

        fun authentication() = object : NavigationAction {
            override val destination = NavigationDestinations.AUTHENTICATION_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
    }

    object LoginScreen {
        fun home() = object : NavigationAction {
            override val destination = NavigationDestinations.HOME_SCREEN
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
        fun authentication() = object : NavigationAction {
            override val destination = NavigationDestinations.AUTHENTICATION_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
    }
}
