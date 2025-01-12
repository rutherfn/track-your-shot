package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

data class ViewPlayerReportsParams(
    val state: ViewPlayerReportsState,
    val onToolbarMenuClicked: () -> Unit,
    val onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    val onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit
)
