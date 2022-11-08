package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions as Actions

class NavigationActionsTest {

    @Nested inner class NavigationActions {

        @Test fun navigateToHome() {
            Assertions.assertEquals(Actions.SplashScreen.navigateToHome().destination, NavigationDestinations.HOME_SCREEN)
            Assertions.assertEquals(
                Actions.SplashScreen.navigateToHome().navOptions,
                NavOptions.Builder()
                    .setPopUpTo(0, true)
                    .setLaunchSingleTop(true)
                    .build()
            )
        }

        @Test fun navigateToSplash() {
            Assertions.assertEquals(Actions.HomeScreen.navigateToSplash().destination, NavigationDestinations.SPLASH_SCREEN)
        }
    }
}
