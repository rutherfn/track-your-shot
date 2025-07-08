package com.nicholas.rutherford.track.your.shot.navigation

import android.net.Uri

object NavigationDestinationsWithParams {

    fun shotsListScreenWithParams(shouldShowAllPlayersShots: Boolean): String {
        return "${NavigationDestinations.SHOTS_LIST_SCREEN}/$shouldShowAllPlayersShots"
    }
    fun accountInfoWithParams(username: String, email: String): String {
        return "${NavigationDestinations.ACCOUNT_INFO_SCREEN}/$username/$email"
    }

    fun authenticationWithParams(username: String, email: String): String {
        return "authenticationScreen?username=${Uri.encode(username)}&email=${Uri.encode(email)}"
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
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    ): String {
        return "${NavigationDestinations.LOG_SHOT_SCREEN}/$isExistingPlayer/$playerId/$shotType/$shotId/$viewCurrentExistingShot/$viewCurrentPendingShot/$fromShotList"
    }

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
