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
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean
    ): String = "${NavigationDestinations.LOG_SHOT_SCREEN}/$isExistingPlayer/$playerId/$shotType/$shotId/$viewCurrentExistingShot/$viewCurrentPendingShot"

    fun termsConditionsWithParams(
        isAcknowledgeConditions: Boolean
    ): String = "${NavigationDestinations.TERMS_CONDITIONS_SCREEN}/$isAcknowledgeConditions"

    fun selectShotWithParams(
        isExistingPlayer: Boolean,
        playerId: Int
    ): String {
        return "${NavigationDestinations.SELECT_SHOT_SCREEN}/$isExistingPlayer/$playerId"
    }
}
