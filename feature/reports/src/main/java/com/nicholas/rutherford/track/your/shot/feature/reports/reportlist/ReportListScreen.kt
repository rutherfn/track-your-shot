package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Main screen composable for displaying a list of player reports.
 *
 * Shows a list of player reports if reports exist; otherwise, displays an empty state
 * with a message depending on whether the user has permission to view reports.
 *
 * @param params Contains the current UI state and event handlers for this screen.
 */
@Composable
fun ReportListScreen(params: ReportListParams) {
    if (!params.state.hasNoReports) {
        ReportList(
            state = params.state,
            onViewReportClicked = params.onViewReportClicked,
            onDeletePlayerReportClicked = params.onDeletePlayerReportClicked,
            onDownloadPlayerReportClicked = params.onDownloadPlayerReportClicked,
            buildDateTimeStamp = params.buildDateTimeStamp
        )
    } else {
        ReportListEmptyState(
            description = if (params.state.hasNoReportPermission) {
                stringResource(id = StringsIds.atLeastOnePlayerDescription)
            } else {
                stringResource(id = StringsIds.hintAddNewReport)
            }
        )
    }
}

/**
 * Displays a scrollable list of individual player reports.
 *
 * Each report is shown using the [PlayerReport] composable.
 *
 * @param state The current state containing the list of reports to display.
 * @param onViewReportClicked Callback invoked when the user chooses to view a report (URL passed).
 * @param onDeletePlayerReportClicked Callback invoked when the user chooses to delete a report.
 * @param onDownloadPlayerReportClicked Callback invoked when the user chooses to download a report.
 * @param buildDateTimeStamp Function to format a timestamp (Long) into a user-readable string.
 */
@Composable
fun ReportList(
    state: ReportListState,
    onViewReportClicked: (url: String) -> Unit,
    onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    buildDateTimeStamp: (value: Long) -> String
) {
    LazyColumn {
        items(state.reports) { report ->
            PlayerReport(
                report = report,
                onViewReportClicked = onViewReportClicked,
                onDeletePlayerReportClicked = onDeletePlayerReportClicked,
                onDownloadPlayerReportClicked = onDownloadPlayerReportClicked,
                buildDateTimeStamp = buildDateTimeStamp
            )
        }
    }
}

/**
 * Displays an individual player report item with options to view, download, or delete the report.
 *
 * The report is shown inside a Card with the player's name, report creation date, and a menu button.
 *
 * @param report The data for the individual player report to display.
 * @param onViewReportClicked Callback triggered when the user selects to view the report.
 * @param onDeletePlayerReportClicked Callback triggered when the user selects to delete the report.
 * @param onDownloadPlayerReportClicked Callback triggered when the user selects to download the report.
 * @param buildDateTimeStamp Function to format the report's timestamp for display.
 */
@Composable
fun PlayerReport(
    report: IndividualPlayerReport,
    onViewReportClicked: (url: String) -> Unit,
    onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    buildDateTimeStamp: (value: Long) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = StringsIds.xReport, report.playerName),
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyles.bodyBold,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(Padding.four))
                    Text(
                        text = stringResource(id = StringsIds.xCreated, buildDateTimeStamp(report.loggedDateValue)),
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyles.bodySmall,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = ""
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        modifier = Modifier.background(AppColors.White),
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.view_report)) },
                            onClick = {
                                expanded = false
                                onViewReportClicked(report.pdfUrl)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.download_report)) },
                            onClick = {
                                expanded = false
                                onDownloadPlayerReportClicked(report)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.delete_report)) },
                            onClick = {
                                expanded = false
                                onDeletePlayerReportClicked(report)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays an empty state UI when there are no reports to show.
 *
 * Shows a centered message describing why no reports are visible and encourages user action.
 *
 * @param description The text message describing the empty state.
 */
@Composable
fun ReportListEmptyState(description: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = StringsIds.noReportsGenerated),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = description,
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Preview of the [ReportListScreen] when there are no reports to display.
 * Shows the empty state UI with appropriate messages.
 */
@Preview(showBackground = true)
@Composable
fun ReportListScreenEmptyStatePreview() {
    ReportListScreen(
        params = ReportListParams(
            state = ReportListState(
                reports = emptyList(),
                hasNoReports = true,
                hasNoReportPermission = false
            ),
            onViewReportClicked = {},
            onDeletePlayerReportClicked = {},
            onDownloadPlayerReportClicked = {},
            buildDateTimeStamp = { timestamp -> "Jan 01, 2024" },
            onAddReportClicked = {},
            onToolbarMenuClicked = {}
        )
    )
}

/**
 * Preview of the [ReportListScreen] with a list containing a single sample player report.
 * Displays how an individual report appears within the list.
 */
@Preview(showBackground = true)
@Composable
fun ReportListScreenWithItemsPreview() {
    val sampleReport = IndividualPlayerReport(
        id = 1,
        playerName = "John Doe",
        pdfUrl = "https://example.com/report.pdf",
        loggedDateValue = 1680000000000L,
        firebaseKey = "firebase"
    )

    ReportListScreen(
        params = ReportListParams(
            state = ReportListState(
                reports = listOf(sampleReport),
                hasNoReports = false,
                hasNoReportPermission = false
            ),
            onViewReportClicked = {},
            onDeletePlayerReportClicked = {},
            onDownloadPlayerReportClicked = {},
            buildDateTimeStamp = { timestamp -> "Mar 27, 2023" },
            onAddReportClicked = {},
            onToolbarMenuClicked = {}
        )
    )
}
