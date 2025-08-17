package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Holds the UI state for the Report List screen.
 *
 * @property reports A list of [IndividualPlayerReport] objects representing all player reports available to display. Defaults to an empty list.
 * @property hasNoReportPermission Indicates whether the current user lacks permission to view reports.
 * @property hasNoReports Indicates whether there are no reports available to show (e.g., empty list).
 */
data class ReportListState(
    val reports: List<IndividualPlayerReport> = emptyList(),
    val hasNoReportPermission: Boolean = false,
    val hasNoReports: Boolean = false
)
