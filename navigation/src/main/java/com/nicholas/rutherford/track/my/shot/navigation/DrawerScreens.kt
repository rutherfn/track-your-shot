package com.nicholas.rutherford.track.my.shot.navigation

import androidx.navigation.NavOptions

sealed class DrawerScreens(
    val title: String,
    val route: String,
    val navOptions: NavOptions
) {
    data object PlayersList : DrawerScreens(
        title = "Players",
        route = "playersListScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )
    data object Settings : DrawerScreens(
        title = "Settings",
        route = "settingsScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )
}
