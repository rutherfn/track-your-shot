package com.nicholas.rutherford.track.your.shot.feature.statistics

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Aggregated statistics displayed in the statistics snapshot overview.
 *
 * @property totalPlayers Number of players included in the overview.
 * @property totalShotsAttempted Combined shot attempts across all players.
 * @property totalShotsMade Combined made shots across all players.
 * @property totalShotsMissed Combined missed shots across all players.
 * @property averageMadePercentage Team-wide average shooting percentage.
 */
data class StatisticsOverview(
    val totalPlayers: Int,
    val totalShotsAttempted: Int,
    val totalShotsMade: Int,
    val totalShotsMissed: Int,
    val averageMadePercentage: Double
)
