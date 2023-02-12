package com.nicholas.rutherford.track.my.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username"
    }
}
