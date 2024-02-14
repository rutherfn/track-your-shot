package com.nicholas.rutherford.track.your.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }

    fun createEditPlayerWithParams(firstName: String, lastName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN}/$firstName/$lastName"
    }

    fun selectShotWithParams(isExistingPlayer: Boolean, playerId: Int): String {
        return "${NavigationDestinations.SELECT_SHOT_SCREEN}/$isExistingPlayer/$playerId"
    }
}
