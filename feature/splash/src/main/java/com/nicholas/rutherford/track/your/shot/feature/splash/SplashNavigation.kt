package com.nicholas.rutherford.track.your.shot.feature.splash

/**
 * Defines navigation actions available from the splash screen.
 */
interface SplashNavigation {
    fun navigateToAuthentication(username: String, email: String)
    fun navigateToPlayersList()
    fun navigateToLogin()
    fun navigateToTermsAndConditions()
}
