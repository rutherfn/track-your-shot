package com.nicholas.rutherford.track.your.shot.navigation.arguments

import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavArguments {
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
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType },
        navArgument(NamedArguments.CURRENT_PLAYER_SHOTS_SIZE) { type = NavType.IntType }
    )
    val logShot = listOf(
        navArgument(NamedArguments.IS_EXISTING_PLAYER) { type = NavType.BoolType },
        navArgument(NamedArguments.PLAYER_ID) { type = NavType.IntType },
        navArgument(NamedArguments.SHOT_ID) { type = NavType.IntType },
        navArgument(NamedArguments.CURRENT_PLAYER_SHOTS_SIZE) { type = NavType.IntType}
    )
}
