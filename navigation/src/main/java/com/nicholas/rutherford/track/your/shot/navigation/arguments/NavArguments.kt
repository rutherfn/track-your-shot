package com.nicholas.rutherford.track.your.shot.navigation.arguments

import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavArguments {
    val accountInfo = listOf(
        navArgument(NamedArguments.USERNAME) { type = NavType.StringType },
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )
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
    val createEditPlayer = listOf(
        navArgument(NamedArguments.FIRST_NAME) { type = NavType.StringType },
        navArgument(NamedArguments.LAST_NAME) { type = NavType.StringType }
    )
    val shotsList = listOf(
        navArgument(NamedArguments.SHOULD_SHOW_ALL_PLAYERS_SHOTS) { type = NavType.BoolType }
    )
    val selectShot = listOf(
        navArgument(NamedArguments.IS_EXISTING_PLAYER) { type = NavType.BoolType },
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType }
    )
    val logShot = listOf(
        navArgument(NamedArguments.IS_EXISTING_PLAYER) { type = NavType.BoolType },
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType },
        navArgument(NamedArguments.SHOT_TYPE) { type = NavType.IntType },
        navArgument(NamedArguments.SHOT_ID) { type = NavType.IntType },
        navArgument(NamedArguments.VIEW_CURRENT_EXISTING_SHOT) { type = NavType.BoolType },
        navArgument(NamedArguments.VIEW_CURRENT_PENDING_SHOT) { type = NavType.BoolType },
        navArgument(NamedArguments.FROM_SHOT_LIST) { type = NavType.BoolType }
    )
    val onBoardingEducation = listOf(
        navArgument(NamedArguments.IS_FIRST_TIME_LAUNCHED) { type = NavType.BoolType }
    )
    val termsConditions = listOf(
        navArgument(NamedArguments.SHOULD_ACCEPT_TERMS) { type = NavType.BoolType }
    )
    val createEditDeclaredShot = listOf(
        navArgument(NamedArguments.SHOT_NAME) { type = NavType.StringType }
    )
}
