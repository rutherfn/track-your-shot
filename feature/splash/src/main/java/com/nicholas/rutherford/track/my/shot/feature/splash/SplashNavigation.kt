package com.nicholas.rutherford.track.my.shot.feature.splash

interface SplashNavigation {
    fun navigateToAuthentication(username: String, email: String)
    fun navigateToHome(email: String)
    fun navigateToLogin()
}
