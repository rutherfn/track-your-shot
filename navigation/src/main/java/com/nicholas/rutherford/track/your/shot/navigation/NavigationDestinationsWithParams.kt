package com.nicholas.rutherford.track.your.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }

    fun playersListWithParams(shouldUpdate: Boolean): String {
        return "${NavigationDestinations.PLAYERS_LIST_SCREEN}/$shouldUpdate"
    }
}
