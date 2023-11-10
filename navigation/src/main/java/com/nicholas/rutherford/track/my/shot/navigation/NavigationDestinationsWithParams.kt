package com.nicholas.rutherford.track.your.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }
}
