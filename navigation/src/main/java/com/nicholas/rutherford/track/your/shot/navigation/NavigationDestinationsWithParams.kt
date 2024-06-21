package com.nicholas.rutherford.track.your.shot.navigation

object NavigationDestinationsWithParams {
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }

    fun createEditPlayerWithParams(firstName: String, lastName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN}/$firstName/$lastName"
    }

    fun logShotWithParams(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotId: Int,
        currentPlayerShotsSize: Int
    ): String = "${NavigationDestinations.LOG_SHOT_SCREEN}/$isExistingPlayer/$playerId/$shotId/$currentPlayerShotsSize"

    fun selectShotWithParams(
        isExistingPlayer: Boolean,
        playerId: Int,
        currentPlayerShotsSize: Int
    ): String = "${NavigationDestinations.SELECT_SHOT_SCREEN}/$isExistingPlayer/$playerId/$currentPlayerShotsSize"
}
