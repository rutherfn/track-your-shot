package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import com.nicholas.rutherford.track.your.shot.feature.statistics.PlayerStatisticsSummary

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Holds the UI state for the Selected Player Statistics screen.
 *
 * @property playerStatistics List of aggregated statistics for the selected player.
 */
data class PlayerStatisticsState(
    val playerStatistics: List<PlayerStatisticsSummary> = emptyList(),
)
