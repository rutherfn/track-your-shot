package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

data class ReportListParams(
    val state: ReportListState,
    val onToolbarMenuClicked: () -> Unit,
    val onAddReportClicked: () -> Unit,
    val onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    val onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    val buildDateTimeStamp: (value: Long) -> String
)
