package com.nicholas.rutherford.track.your.shot.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Summarize
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds

data object PlayersListAction : DrawerAction(
    titleId = StringsIds.players,
    imageVector = Icons.Filled.Person,
    route = NavigationDestinations.PLAYERS_LIST_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

data object ReportingAction : DrawerAction(
    titleId = StringsIds.reports,
    imageVector = Icons.Filled.Summarize,
    route = NavigationDestinations.REPORTS_LIST_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .setLaunchSingleTop(true)
        .build()
)

data object StatsAction : DrawerAction(
    titleId = StringsIds.stats,
    imageVector = Icons.Filled.Analytics,
    route = NavigationDestinations.STATS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

data object ComparePlayersStatsAction : DrawerAction(
    titleId = StringsIds.comparePlayersStats,
    imageVector = Icons.Filled.Compare,
    route = NavigationDestinations.COMPARE_PLAYERS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

data object VoiceCommandsAction : DrawerAction(
    titleId = StringsIds.voiceCommands,
    imageVector = Icons.Filled.Mic,
    route = NavigationDestinations.VOICE_COMMANDS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

data object ShotsAction : DrawerAction(
    titleId = StringsIds.shots,
    imageVector = Icons.Filled.SportsBasketball,
    route = NavigationDestinationsWithParams.shotsListScreenWithParams(shouldShowAllPlayersShots = true),
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

data object SettingsAction : DrawerAction(
    titleId = StringsIds.settings,
    route = NavigationDestinations.SETTINGS_SCREEN,
    imageVector = Icons.Filled.Settings,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .setLaunchSingleTop(true)
        .build()
)

data object LogoutAction : DrawerAction(
    titleId = StringsIds.logout,
    imageVector = Icons.Filled.Logout,
    route = null,
    navOptions = null
)
