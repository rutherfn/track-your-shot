package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

data class ReportListState(
    val reports: List<IndividualPlayerReport> = emptyList(),
    val hasNoReportPermission: Boolean = false,
    val hasNoReports: Boolean = false
)
