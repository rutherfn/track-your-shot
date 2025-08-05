package com.nicholas.rutherford.track.your.shot.navigation

import android.net.Uri

object NavigationDestinationsWithParams {

    fun buildAuthenticationDestination(username: String?, email: String?): String {
        val queryParams = listOfNotNull(
            username?.takeIf { it.isNotEmpty() }?.let { "username=$it" },
            email?.takeIf { it.isNotEmpty() }?.let { "email=$it" }
        )

        return buildString {
            append("authenticationScreen")
            if (queryParams.isNotEmpty()) {
                append("?")
                append(queryParams.joinToString("&"))
            }
        }
    }

    fun buildTermsConditionsDestination(shouldAcceptTerms: Boolean): String {
        return buildString {
            append("termsConditionsScreen")

            val queryParams = listOf("shouldAcceptTerms=$shouldAcceptTerms")

            append("?")
            append(queryParams.joinToString("&"))
        }
    }

    fun buildLogShotDestination(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    ): String {
        val queryParams = listOf(
            "isExistingPlayer=$isExistingPlayer",
            "playerId=$playerId",
            "shotType=$shotType",
            "shotId=$shotId",
            "viewCurrentExistingShot=$viewCurrentExistingShot",
            "viewCurrentPendingShot=$viewCurrentPendingShot",
            "fromShotList=$fromShotList"
        )

        return buildString {
            append("logShotScreen")
            append("?")
            append(queryParams.joinToString("&"))
        }
    }

    fun shotsListScreenWithParams(shouldShowAllPlayersShots: Boolean): String {
        return "shotsListScreen?shouldShowAllPlayersShots=$shouldShowAllPlayersShots"
    }

    fun buildCreateEditDeclaredShotDestination(shotName: String): String = "createEditDeclaredShotsScreen?shotName=$shotName"

    fun accountInfoWithParams(username: String, email: String): String = "accountInfoScreen?username=${Uri.encode(username)}&email=${Uri.encode(email)}"

    fun authenticationWithParams(username: String, email: String): String {
        return "authenticationScreen?username=${Uri.encode(username)}&email=${Uri.encode(email)}"
    }

    fun termsConditionsWithParams(shouldAcceptTerms: Boolean): String {
        return "termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms"
    }

    fun createEditPlayerWithParams(firstName: String, lastName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN}/$firstName/$lastName"
    }
}
