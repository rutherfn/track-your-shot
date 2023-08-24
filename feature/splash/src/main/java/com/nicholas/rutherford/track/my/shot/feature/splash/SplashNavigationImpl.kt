package com.nicholas.rutherford.track.my.shot.feature.splash

import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class SplashNavigationImpl(private val navigator: Navigator) : SplashNavigation {

    override fun navigateToAuthentication(username: String, email: String) = navigator.navigate(navigationAction = NavigationActions.SplashScreen.authentication(username = username, email = email))

    override fun navigateToPlayersList() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.playersList())

    override fun navigateToLogin() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.login())
}
