package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * [NavigationDestinationsWithParams] provides utility functions to dynamically
 * build navigation routes that include query parameters.
 *
 * This class ensures that routes are consistent, encoded properly,
 * and use centralized constants from [NavigationDestinations].
 *
 * All parameter keys and base route names are managed separately in [NavigationDestinations]
 * to avoid hardcoded strings and improve maintainability.
 */
object NavigationDestinationsWithParams {

    /**
     * Builds the authentication screen route with optional query parameters for username and email.
     *
     * @param username Optional username value to pass to the screen.
     * @param email Optional email value to pass to the screen.
     * @return A route string like: `authenticationScreen?username=...&email=...`
     */
    fun buildAuthenticationDestination(username: String?, email: String?): String {
        val queryParams = listOfNotNull(
            username?.takeIf { it.isNotEmpty() }?.let { "${NavigationDestinations.PARAM_USERNAME}=${UriEncoder.encode(it)}" },
            email?.takeIf { it.isNotEmpty() }?.let { "${NavigationDestinations.PARAM_EMAIL}=${UriEncoder.encode(it)}" }
        )

        return buildString {
            append(NavigationDestinations.AUTHENTICATION_SCREEN)
            if (queryParams.isNotEmpty()) {
                append("?")
                append(queryParams.joinToString("&"))
            }
        }
    }

    /**
     * Builds the terms and conditions screen route with a flag indicating whether
     * the user should accept the terms.
     *
     * @param shouldAcceptTerms Whether the user must accept the terms.
     * @return A route string like: `termsConditionsScreen?shouldAcceptTerms=true`
     */
    fun buildTermsConditionsDestination(shouldAcceptTerms: Boolean): String {
        return "${NavigationDestinations.TERMS_CONDITIONS_SCREEN}?" +
            "${NavigationDestinations.PARAM_SHOULD_ACCEPT_TERMS}=$shouldAcceptTerms"
    }

    /**
     * Builds the log shot screen route with multiple parameters.
     *
     * @return A route string like: `logShotScreen?isExistingPlayer=true&playerId=1...`
     */
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
            "${NavigationDestinations.PARAM_IS_EXISTING_PLAYER}=$isExistingPlayer",
            "${NavigationDestinations.PARAM_PLAYER_ID}=$playerId",
            "${NavigationDestinations.PARAM_SHOT_TYPE}=$shotType",
            "${NavigationDestinations.PARAM_SHOT_ID}=$shotId",
            "${NavigationDestinations.PARAM_VIEW_CURRENT_EXISTING_SHOT}=$viewCurrentExistingShot",
            "${NavigationDestinations.PARAM_VIEW_CURRENT_PENDING_SHOT}=$viewCurrentPendingShot",
            "${NavigationDestinations.PARAM_FROM_SHOT_LIST}=$fromShotList"
        )

        return "${NavigationDestinations.LOG_SHOT_SCREEN}?${queryParams.joinToString("&")}"
    }

    /**
     * Builds the shots list screen route with a flag indicating whether
     * to show all players' shots.
     *
     * @param shouldShowAllPlayersShots Flag to control filtering in the UI.
     * @return A route string like: `shotsListScreen?shouldShowAllPlayersShots=true`
     */
    fun shotsListScreenWithParams(shouldShowAllPlayersShots: Boolean): String {
        return "${NavigationDestinations.SHOTS_LIST_SCREEN}?" +
            "${NavigationDestinations.PARAM_SHOULD_SHOW_ALL_PLAYERS_SHOTS}=$shouldShowAllPlayersShots"
    }

    /**
     * Builds the create/edit declared shot screen route with a shot name.
     *
     * @param shotName The name of the shot to edit or create.
     * @return A route string like: `createEditDeclaredShotsScreen?shotName=FreeThrow`
     */
    fun buildCreateEditDeclaredShotDestination(shotName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN}?" +
            "${NavigationDestinations.PARAM_SHOT_NAME}=${UriEncoder.encode(shotName)}"
    }

    /**
     * Builds the account info screen route with a username and email.
     *
     * @param username The username to display or update.
     * @param email The user's email.
     * @return A route string like: `accountInfoScreen?username=Nick&email=nick@email.com`
     */
    fun accountInfoWithParams(username: String, email: String): String {
        return "${NavigationDestinations.ACCOUNT_INFO_SCREEN}?" +
            "${NavigationDestinations.PARAM_USERNAME}=${UriEncoder.encode(username)}&" +
            "${NavigationDestinations.PARAM_EMAIL}=${UriEncoder.encode(email)}"
    }

    /**
     * Builds the authentication screen route with a username and email.
     * This is a simplified version for non-null values.
     *
     * @param username The username to pass.
     * @param email The email to pass.
     */
    fun authenticationWithParams(username: String, email: String): String {
        return "${NavigationDestinations.AUTHENTICATION_SCREEN}?" +
            "${NavigationDestinations.PARAM_USERNAME}=${UriEncoder.encode(username)}&" +
            "${NavigationDestinations.PARAM_EMAIL}=${UriEncoder.encode(email)}"
    }

    /**
     * Builds the create/edit player screen route using path parameters.
     *
     * @param firstName The player's first name.
     * @param lastName The player's last name.
     * @return A route like: `createEditPlayerScreen/Nick/Rutherford`
     */
    fun createEditPlayerWithParams(firstName: String, lastName: String): String {
        return "${NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN}/$firstName/$lastName"
    }
}
