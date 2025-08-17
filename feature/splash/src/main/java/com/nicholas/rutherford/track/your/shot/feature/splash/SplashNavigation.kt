package com.nicholas.rutherford.track.your.shot.feature.splash

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the splash screen.
 */
interface SplashNavigation {
    fun navigateToAuthentication(username: String, email: String)
    fun navigateToPlayersList()
    fun navigateToLogin()
    fun navigateToTermsAndConditions()
}
