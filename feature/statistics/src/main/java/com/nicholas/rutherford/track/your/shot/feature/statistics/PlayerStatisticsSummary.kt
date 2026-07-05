package com.nicholas.rutherford.track.your.shot.feature.statistics

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-02
 *
 * Represents aggregated shooting statistics for a single player.
 *
 * @property playerId Unique identifier for the player.
 * @property playerName Full name of the player.
 * @property totalShotsAttempted Total number of shot attempts across all logged sessions.
 * @property totalShotsMade Total number of successful shots across all logged sessions.
 * @property totalShotsMissed Total number of missed shots across all logged sessions.
 * @property overallMadePercentage Overall shooting percentage based on made shots divided by attempts.
 * @property loggedShotsCount Number of finalized shot logs included in the summary.
 */
data class PlayerStatisticsSummary(
    val playerId: Int,
    val playerName: String,
    val totalShotsAttempted: Int,
    val totalShotsMade: Int,
    val totalShotsMissed: Int,
    val overallMadePercentage: Double,
    val loggedShotsCount: Int
)
