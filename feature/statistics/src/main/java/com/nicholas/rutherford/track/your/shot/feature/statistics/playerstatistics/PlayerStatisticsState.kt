package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChartInfo
import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Holds the UI state for the Selected Player Statistics screen.
 *
 * @property playerStatisticsSummary Aggregated statistics for the selected player.
 * @property loggedShotEntries All finalized logged shots for the player.
 * @property dateFilterOptions Available date filter options including all dates.
 * @property selectedDateFilter Currently selected date filter option.
 * @property allDatesFilterLabel Label used for the all-dates filter option.
 * @property chartInfo Line chart data filtered by [selectedDateFilter].
 * @property hasNoLoggedShots Whether the player has no finalized logged shots to display.
 */
data class PlayerStatisticsState(
    val playerStatisticsSummary: PlayerStatisticsSummary? = null,
    val loggedShotEntries: List<PlayerLoggedShotEntry> = emptyList(),
    val dateFilterOptions: List<String> = emptyList(),
    val selectedDateFilter: String = "",
    val allDatesFilterLabel: String = "",
    val chartInfo: PlayerShotsBreakdownLineChartInfo? = null,
    val hasNoLoggedShots: Boolean = true
)
