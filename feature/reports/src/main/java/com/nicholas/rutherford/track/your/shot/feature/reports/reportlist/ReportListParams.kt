package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

data class ReportListParams(
    val onToolbarMenuClicked: () -> Unit,
    val onHelpClicked: () -> Unit,
    val onReportItemClicked: (index: Int) -> Unit,
    val state: ReportListState
)
