package com.nicholas.rutherford.track.your.shot.compose.components.charts

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Represents made and missed shot percentages across logged sessions for a line chart.
 *
 * @property madeLabel Label displayed for the made shots line.
 * @property missedLabel Label displayed for the missed shots line.
 * @property entries Logged session data points plotted on the chart.
 */
data class PlayerShotsBreakdownLineChartInfo(
    val madeLabel: String,
    val missedLabel: String,
    val entries: List<PlayerShotsBreakdownLineChartEntry>
)
