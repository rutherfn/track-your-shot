package com.nicholas.rutherford.track.your.shot.feature.splash

interface SplashNavigation {
    fun navigateToAuthentication(username: String, email: String)
    fun navigateToPlayersList()
    fun navigateToLogin()
    fun navigateToTermsAndConditions()
}
