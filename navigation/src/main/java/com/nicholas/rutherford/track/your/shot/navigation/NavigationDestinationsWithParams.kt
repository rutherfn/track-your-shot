package com.nicholas.rutherford.track.your.shot.navigation

import android.net.Uri

object NavigationDestinationsWithParams {

    fun shotsListScreenWithParams(shouldShowAllPlayersShots: Boolean): String {
        return "shotsListScreen?shouldShowAllPlayersShots=$shouldShowAllPlayersShots"
    }
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
