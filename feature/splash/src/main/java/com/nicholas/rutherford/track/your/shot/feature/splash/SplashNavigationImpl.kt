package com.nicholas.rutherford.track.your.shot.feature.splash

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [SplashNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class SplashNavigationImpl(private val navigator: Navigator) : SplashNavigation {

    override fun navigateToAuthentication(username: String, email: String) = navigator.navigate(navigationAction = NavigationActions.SplashScreen.authentication(username = username, email = email))

    override fun navigateToPlayersList() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.playersList())

    override fun navigateToLogin() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.login())

    override fun navigateToTermsAndConditions() = navigator.navigate(navigationAction = NavigationActions.SplashScreen.termsConditions(shouldAcceptTerms = true))
}
