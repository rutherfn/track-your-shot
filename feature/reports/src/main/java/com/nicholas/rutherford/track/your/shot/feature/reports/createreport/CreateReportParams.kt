package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

data class CreateReportParams(
    val onToolbarMenuClicked: () -> Unit,
    val updatePlayersState: () -> Unit,
    val showCreatePlayerReportNotification: () -> Unit,
    val state: CreateReportState,
    val shouldRefreshData: Boolean
)
