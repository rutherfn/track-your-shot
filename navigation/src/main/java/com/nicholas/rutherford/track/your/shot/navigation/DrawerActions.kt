package com.nicholas.rutherford.track.your.shot.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Summarize
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Defines all available navigation drawer actions within the application.
 *
 * Each action is represented as a `data object` extending [DrawerAction].
 * These objects provide the necessary title, icon, navigation route,
 * and [NavOptions] to configure behavior when navigating from the drawer.
 */

/** Navigate to the players list screen. */
data object PlayersListAction : DrawerAction(
    titleId = StringsIds.players,
    imageVector = Icons.Filled.Person,
    route = NavigationDestinations.PLAYERS_LIST_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

/** Navigate to the reports list screen. */
data object ReportingAction : DrawerAction(
    titleId = StringsIds.reports,
    imageVector = Icons.Filled.Summarize,
    route = NavigationDestinations.REPORTS_LIST_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .setLaunchSingleTop(true)
        .build()
)

/** Navigate to the stats screen. */
data object StatsAction : DrawerAction(
    titleId = StringsIds.stats,
    imageVector = Icons.Filled.Analytics,
    route = NavigationDestinations.STATS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

/** Navigate to the compare players stats screen. */
data object ComparePlayersStatsAction : DrawerAction(
    titleId = StringsIds.comparePlayersStats,
    imageVector = Icons.Filled.Compare,
    route = NavigationDestinations.COMPARE_PLAYERS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

/** Navigate to the voice commands screen. */
data object VoiceCommandsAction : DrawerAction(
    titleId = StringsIds.voiceCommands,
    imageVector = Icons.Filled.Mic,
    route = NavigationDestinations.VOICE_COMMANDS_SCREEN,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

/** Navigate to the shots list screen, showing all players' shots. */
data object ShotsAction : DrawerAction(
    titleId = StringsIds.shots,
    imageVector = Icons.Filled.SportsBasketball,
    route = NavigationDestinationsWithParams.shotsListScreenWithParams(shouldShowAllPlayersShots = true),
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .build()
)

/** Navigate to the settings screen. */
data object SettingsAction : DrawerAction(
    titleId = StringsIds.settings,
    route = NavigationDestinations.SETTINGS_SCREEN,
    imageVector = Icons.Filled.Settings,
    navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .setLaunchSingleTop(true)
        .build()
)

/**
 * Represents a logout action.
 *
 * Unlike other actions, this does not specify a navigation route or [NavOptions],
 * This is because we check to see if this is null back in the navigation component.
 * If it is null, we don't navigate and instead just log the user out.
 */
data object LogoutAction : DrawerAction(
    titleId = StringsIds.logout,
    imageVector = Icons.AutoMirrored.Filled.Logout,
    route = null,
    navOptions = null
)
