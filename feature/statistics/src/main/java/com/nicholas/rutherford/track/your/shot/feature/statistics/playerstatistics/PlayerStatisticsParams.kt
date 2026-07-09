package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Parameters used to render the Player Statistics screen UI.
 *
 * @property state The current state of the Player Statistics screen.
 * @property onToolbarMenuClicked Callback triggered when the toolbar back icon is clicked.
 * @property onDateFilterSelected Callback triggered when a date filter chip is selected.
 * @property formatPercentage Formats a numeric percentage value into a display string.
 */
data class PlayerStatisticsParams(
    val state: PlayerStatisticsState,
    val onToolbarMenuClicked: () -> Unit,
    val onDateFilterSelected: (filter: String) -> Unit,
    val formatPercentage: (value: Double) -> String
)
