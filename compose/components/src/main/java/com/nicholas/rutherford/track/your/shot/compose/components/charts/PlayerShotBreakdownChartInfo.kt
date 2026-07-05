package com.nicholas.rutherford.track.your.shot.compose.components.charts

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Represents made and missed shot counts for a pie chart breakdown.
 *
 * @property madeLabel Label displayed for made shots.
 * @property missedLabel Label displayed for missed shots.
 * @property shotsMade Total number of made shots.
 * @property shotsMissed Total number of missed shots.
 */
data class PlayerShotBreakdownChartInfo(
    val madeLabel: String,
    val missedLabel: String,
    val shotsMade: Int,
    val shotsMissed: Int
)
