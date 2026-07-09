package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Represents a finalized logged shot entry used to plot player statistics over time.
 *
 * @property loggedDateLabel Formatted date label used for date filtering.
 * @property loggedDateMillis Raw logged date value in milliseconds.
 * @property shotName Name of the logged shot session displayed on the chart.
 * @property madePercentage Percentage of made shots for the session (0–100).
 * @property missedPercentage Percentage of missed shots for the session (0–100).
 */
data class PlayerLoggedShotEntry(
    val loggedDateLabel: String,
    val loggedDateMillis: Long,
    val shotName: String,
    val madePercentage: Double,
    val missedPercentage: Double
)
