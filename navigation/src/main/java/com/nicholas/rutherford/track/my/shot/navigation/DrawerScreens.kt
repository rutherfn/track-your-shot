package com.nicholas.rutherford.track.my.shot.navigation

sealed class DrawerScreens(
    val title: String,
    val route: String
) {
    data object PlayersList : DrawerScreens(
        title = "Players",
        route = "playersListScreen"
    )
    data object Settings : DrawerScreens(
        title = "Settings",
        route = "settingsScreen"
    )
}
