package com.nicholas.rutherford.track.your.shot.navigation.arguments

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Defines reusable sets of navigation arguments for different routes
 * within the application. Each property contains a list of [navArgument]
 * configurations that specify the expected argument names, types,
 * nullability, and default values where applicable.
 */
object NavArguments {

    /** Arguments required when navigating with account info (username, email). */
    val accountInfo = listOf(
        navArgument(NamedArguments.USERNAME) { type = NavType.StringType },
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )

    /**
     * Arguments used during authentication.
     * Both username and email are optional, nullable, and default to null.
     */
    val authentication = listOf(
        navArgument(NamedArguments.USERNAME) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        },
        navArgument(NamedArguments.EMAIL) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    )

    /** Arguments for creating or editing a player (first and last name). */
    val createEditPlayer = listOf(
        navArgument(NamedArguments.FIRST_NAME) { type = NavType.StringType },
        navArgument(NamedArguments.LAST_NAME) { type = NavType.StringType }
    )

    /** Arguments for the shots list screen. */
    val shotsList = listOf(
        navArgument(NamedArguments.SHOULD_SHOW_ALL_PLAYERS_SHOTS) { type = NavType.BoolType }
    )

    /** Arguments for selecting a shot (existing player status, player ID). */
    val selectShot = listOf(
        navArgument(NamedArguments.IS_EXISTING_PLAYER) { type = NavType.BoolType },
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType }
    )

    /**
     * Arguments for logging a shot.
     * Includes player details, shot type, shot ID, and various flags for
     * viewing and navigation context.
     */
    val logShot = listOf(
        navArgument(NamedArguments.IS_EXISTING_PLAYER) { type = NavType.BoolType },
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType },
        navArgument(NamedArguments.SHOT_TYPE) { type = NavType.IntType },
        navArgument(NamedArguments.SHOT_ID) { type = NavType.IntType },
        navArgument(NamedArguments.VIEW_CURRENT_EXISTING_SHOT) { type = NavType.BoolType },
        navArgument(NamedArguments.VIEW_CURRENT_PENDING_SHOT) { type = NavType.BoolType },
        navArgument(NamedArguments.FROM_SHOT_LIST) { type = NavType.BoolType }
    )

    /** Argument for onboarding education (first-time launch flag). */
    val onBoardingEducation = listOf(
        navArgument(NamedArguments.IS_FIRST_TIME_LAUNCHED) { type = NavType.BoolType }
    )

    /** Argument for terms and conditions acceptance. */
    val termsConditions = listOf(
        navArgument(NamedArguments.SHOULD_ACCEPT_TERMS) { type = NavType.BoolType }
    )

    /** Argument for creating or editing a declared shot (shot name). */
    val createEditDeclaredShot = listOf(
        navArgument(NamedArguments.SHOT_NAME) { type = NavType.StringType }
    )
}
