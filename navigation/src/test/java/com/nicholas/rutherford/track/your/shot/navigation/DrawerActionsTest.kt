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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DrawerActionsTest {

    @Test
    fun playersListAction() {
        Assertions.assertEquals(
            PlayersListAction.titleId,
            StringsIds.players
        )
        Assertions.assertEquals(
            PlayersListAction.imageVector,
            Icons.Filled.Person
        )
        Assertions.assertEquals(
            PlayersListAction.route,
            NavigationDestinations.PLAYERS_LIST_SCREEN
        )
        Assertions.assertEquals(
            PlayersListAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        )
    }

    @Test
    fun reportingAction() {
        Assertions.assertEquals(
            ReportingAction.titleId,
            StringsIds.reports
        )
        Assertions.assertEquals(
            ReportingAction.imageVector,
            Icons.Filled.Summarize
        )
        Assertions.assertEquals(
            ReportingAction.route,
            NavigationDestinations.REPORTS_LIST_SCREEN
        )
        Assertions.assertEquals(
            ReportingAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        )
    }

    @Test
    fun statsAction() {
        Assertions.assertEquals(
            StatsAction.titleId,
            StringsIds.stats
        )
        Assertions.assertEquals(
            StatsAction.imageVector,
            Icons.Filled.Analytics
        )
        Assertions.assertEquals(
            StatsAction.route,
            NavigationDestinations.STATS_SCREEN
        )
        Assertions.assertEquals(
            StatsAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        )
    }

    @Test
    fun comparePlayerStatsAction() {
        Assertions.assertEquals(
            ComparePlayersStatsAction.titleId,
            StringsIds.comparePlayersStats
        )
        Assertions.assertEquals(
            ComparePlayersStatsAction.imageVector,
            Icons.Filled.Compare
        )
        Assertions.assertEquals(
            ComparePlayersStatsAction.route,
            NavigationDestinations.COMPARE_PLAYERS_SCREEN
        )
        Assertions.assertEquals(
            ComparePlayersStatsAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        )
    }

    @Test
    fun voiceCommandsAction() {
        Assertions.assertEquals(
            VoiceCommandsAction.titleId,
            StringsIds.voiceCommands
        )
        Assertions.assertEquals(
            VoiceCommandsAction.imageVector,
            Icons.Filled.Mic
        )
        Assertions.assertEquals(
            VoiceCommandsAction.route,
            NavigationDestinations.VOICE_COMMANDS_SCREEN
        )
        Assertions.assertEquals(
            VoiceCommandsAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        )
    }

    @Test
    fun shotsAction() {
        Assertions.assertEquals(
            ShotsAction.titleId,
            StringsIds.shots
        )
        Assertions.assertEquals(
            ShotsAction.imageVector,
            Icons.Filled.SportsBasketball
        )
        Assertions.assertEquals(
            ShotsAction.route,
            NavigationDestinations.SHOTS_LIST_SCREEN
        )
        Assertions.assertEquals(
            VoiceCommandsAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        )
    }

    @Test
    fun settingsAction() {
        Assertions.assertEquals(
            SettingsAction.titleId,
            StringsIds.settings
        )
        Assertions.assertEquals(
            SettingsAction.imageVector,
            Icons.Filled.Settings
        )
        Assertions.assertEquals(
            SettingsAction.route,
            NavigationDestinations.SETTINGS_SCREEN
        )
        Assertions.assertEquals(
            SettingsAction.navOptions,
            NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        )
    }

    @Test
    fun logoutAction() {
        Assertions.assertEquals(
            LogoutAction.titleId,
            StringsIds.logout
        )
        Assertions.assertEquals(
            LogoutAction.imageVector,
            Icons.Filled.Logout
        )
        Assertions.assertEquals(
            LogoutAction.route,
            null
        )
        Assertions.assertEquals(
            LogoutAction.navOptions,
            null
        )
    }
}
