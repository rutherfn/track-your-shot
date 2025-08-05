package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Holds the UI state for the Create Report screen.
 *
 * @property selectedPlayer The currently selected [Player], or null if none is selected.
 * @property playerOptions A list of player names available for selection, defaults to an empty list.
 */
data class CreateReportState(
    val selectedPlayer: Player? = null,
    val playerOptions: List<String> = emptyList()
)
