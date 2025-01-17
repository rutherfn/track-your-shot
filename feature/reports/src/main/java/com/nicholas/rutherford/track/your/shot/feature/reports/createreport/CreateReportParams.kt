package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

data class CreateReportParams(
    val onToolbarMenuClicked: () -> Unit,
    val attemptToGeneratePlayerReport: () -> Unit,
    val onPlayerChanged: (playerName: String) -> Unit,
    val state: CreateReportState
)
