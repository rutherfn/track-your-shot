package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Parameters used to render the Report List UI
 *
 * @property state The current state of the Report List screen, containing all report data to be displayed.
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu icon is clicked.
 * @property onAddReportClicked Callback triggered when the add report button is clicked.
 * @property onViewReportClicked Callback triggered when a report item is clicked. The clicked report's URL is passed as a parameter.
 * @property onDeletePlayerReportClicked Callback triggered when the delete report button is clicked. The clicked report is passed as a parameter.
 * @property onDownloadPlayerReportClicked Callback triggered when the download report button is clicked. The clicked report is passed as a parameter.
 * @property buildDateTimeStamp String builder for the date stamp
 */
data class ReportListParams(
    val state: ReportListState,
    val onToolbarMenuClicked: () -> Unit,
    val onAddReportClicked: () -> Unit,
    val onViewReportClicked: (url: String) -> Unit,
    val onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    val onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    val buildDateTimeStamp: (value: Long) -> String
)
