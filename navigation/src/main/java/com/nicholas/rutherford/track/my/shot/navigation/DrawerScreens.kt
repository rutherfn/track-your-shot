package com.nicholas.rutherford.track.my.shot.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds

sealed class DrawerScreens(
    val titleId: Int,
    val imageVector: ImageVector,
    val route: String?,
    val navOptions: NavOptions?
) {
    data object PlayersList : DrawerScreens(
        titleId = StringsIds.players,
        imageVector = Icons.Filled.SportsBasketball,
        route = "playersListScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )

    data object ComparePlayersStats : DrawerScreens(
        titleId = StringsIds.comparePlayersStats,
        imageVector = Icons.Filled.Compare,
        route = "comparePlayersScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )

    data object Logout : DrawerScreens(
        titleId = StringsIds.logout,
        imageVector = Icons.Filled.Logout,
        route = null,
        navOptions = null
    )
    data object Settings : DrawerScreens(
        titleId = StringsIds.settings,
        route = "settingsScreen",
        imageVector = Icons.Filled.Settings,
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )
    data object Stats : DrawerScreens(
        titleId = StringsIds.stats,
        imageVector = Icons.Filled.Analytics,
        route = "statsScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )

    data object VoiceCommands : DrawerScreens(
        titleId = StringsIds.voiceCommands,
        imageVector = Icons.Filled.Mic,
        route = "voiceCommandsScreen",
        navOptions = NavOptions.Builder()
            .setPopUpTo(0, true)
            .setLaunchSingleTop(true)
            .build()
    )
}
