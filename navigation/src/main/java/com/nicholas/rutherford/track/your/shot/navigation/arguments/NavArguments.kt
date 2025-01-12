package com.nicholas.rutherford.track.your.shot.navigation.arguments

import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavArguments {
    val accountInfo = listOf(
        navArgument(NamedArguments.USERNAME) { type = NavType.StringType },
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )
    val authentication = listOf(
        navArgument(NamedArguments.USERNAME) { type = NavType.StringType },
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )
    val createEditPlayer = listOf(
        navArgument(NamedArguments.FIRST_NAME) { type = NavType.StringType },
        navArgument(NamedArguments.LAST_NAME) { type = NavType.StringType }
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
        navArgument(NamedArguments.VIEW_CURRENT_PENDING_SHOT) { type = NavType.BoolType }
    )
    val termsConditions = listOf(
        navArgument(NamedArguments.IS_ACKNOWLEDGE_CONDITIONS) { type = NavType.BoolType }
    )
    val createReport = listOf(
        navArgument(NamedArguments.SHOULD_REFRESH_DATA) { type = NavType.BoolType }
    )
}
