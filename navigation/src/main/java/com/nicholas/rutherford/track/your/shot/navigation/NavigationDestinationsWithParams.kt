package com.nicholas.rutherford.track.your.shot.navigation

object NavigationDestinationsWithParams {
    fun accountInfoWithParams(username: String, email: String): String {
        return "${NavigationDestinations.ACCOUNT_INFO_SCREEN}/$username/$email"
    }

    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}/$username/$email"
    }

    fun createEditPlayerWithParams(firstName: String, lastName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN}/$firstName/$lastName"
    }

    fun createReportWithParams(shouldRefreshData: Boolean): String {
        return "${NavigationDestinations.CREATE_REPORT_SCREEN}/$shouldRefreshData"
    }

    fun logShotWithParams(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean
    ): String {
        return "${NavigationDestinations.LOG_SHOT_SCREEN}/$isExistingPlayer/$playerId/$shotType/$shotId/$viewCurrentExistingShot/$viewCurrentPendingShot"
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
