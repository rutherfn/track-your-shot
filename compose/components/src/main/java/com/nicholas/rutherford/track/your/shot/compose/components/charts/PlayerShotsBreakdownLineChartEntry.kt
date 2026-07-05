package com.nicholas.rutherford.track.your.shot.compose.components.charts

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Represents a single logged session plotted on [PlayerShotsBreakdownLineChart].
 *
 * @property sessionLabel Display label for the logged session on the x-axis.
 * @property madePercentage Percentage of made shots for the session (0–100).
 * @property missedPercentage Percentage of missed shots for the session (0–100).
 */
data class PlayerShotsBreakdownLineChartEntry(
    val sessionLabel: String,
    val madePercentage: Double,
    val missedPercentage: Double
)
