package com.nicholas.rutherford.track.my.shot.feature.splash

import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class SplashNavigationImpl(private val navigator: Navigator) : SplashNavigation {

    override fun navigateToHome() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.home())

    override fun navigateToLogin() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.login())
}
