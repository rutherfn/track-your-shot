package com.nicholas.rutherford.track.my.shot.navigation

object NavigationActions {

    object SplashScreen {
        fun navigateToHome() = object : NavigationAction {
            override val destination = NavigationDestinations.HOME_SCREEN
        }
    }

    object HomeScreen {
        fun navigateToSplash() = object : NavigationAction {
            override val destination = NavigationDestinations.SPLASH_SCREEN
        }
    }
}
