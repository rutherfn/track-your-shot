package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Holds callback handlers and UI state parameters for the Create Report screen.
 *
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu is clicked.
 * @property attemptToGeneratePlayerReport Callback triggered to start generating a player report.
 * @property onPlayerChanged Callback triggered when the selected player changes, providing the new player name.
 * @property state The current UI state of the Create Report screen
 */
data class CreateReportParams(
    val onToolbarMenuClicked: () -> Unit,
    val attemptToGeneratePlayerReport: () -> Unit,
    val onPlayerChanged: (playerName: String) -> Unit,
    val state: CreateReportState
)
