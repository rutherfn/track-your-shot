package com.nicholas.rutherford.track.my.shot.navigation.arguments

import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavArguments {
    val authentication = listOf(
        navArgument(
            NamedArguments.USERNAME
        ) { type = NavType.StringType },
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )
    val home = listOf(
        navArgument(NamedArguments.EMAIL) { type = NavType.StringType }
    )
}
