package com.nicholas.rutherford.track.your.shot.feature.statistics.main

import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Parameters used to render the Statistics screen UI.
 *
 * @property state The current state of the Statistics screen, containing all statistics data to be displayed.
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu icon is clicked.
 * @property onHelpClicked Callback triggered when the help icon is clicked.
 * @property onPlayerFilterSelected Callback triggered when a player filter chip is selected.
 * @property onViewDetailedStatsClicked Callback triggered when the user taps to view detailed stats for a player.
 * @property formatPercentage Formats a numeric percentage value into a display string.
 */
data class StatisticsParams(
    val state: StatisticsState,
    val onToolbarMenuClicked: () -> Unit,
    val onHelpClicked: () -> Unit,
    val onPlayerFilterSelected: (filter: String) -> Unit,
    val onViewDetailedStatsClicked: (playerStatisticsSummary: PlayerStatisticsSummary) -> Unit,
    val formatPercentage: (value: Double) -> String
)
