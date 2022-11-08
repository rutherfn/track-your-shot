package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions

object NavigationActions {

    object SplashScreen {
        fun navigateToHome() = object : NavigationAction {
            override val destination = NavigationDestinations.HOME_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object HomeScreen {
        fun navigateToSplash() = object : NavigationAction {
            override val destination = NavigationDestinations.SPLASH_SCREEN
        }
    }
}
