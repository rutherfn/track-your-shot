package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions

object NavigationActions {

    object SplashScreen {
        fun home() = object : NavigationAction {
            override val destination = NavigationDestinations.HOME_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object HomeScreen {
        fun splash() = object : NavigationAction {
            override val destination = NavigationDestinations.SPLASH_SCREEN
        }
    }
}
