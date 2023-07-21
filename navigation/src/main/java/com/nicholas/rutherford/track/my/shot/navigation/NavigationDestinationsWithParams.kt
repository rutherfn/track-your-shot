package com.nicholas.rutherford.track.my.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }

    fun homeWithParams(email: String): String {
        return "${NavigationDestinations.HOME_SCREEN}/$email"
    }
}
