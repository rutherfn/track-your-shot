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
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun ReportListScreen(params: ReportListParams) {
    Content(
        ui = {
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
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.reports),
            shouldShowMiddleContentAppBar = true,
            shouldShowSecondaryButton = !params.state.hasNoReportPermission,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onAddReportClicked.invoke() }
        )
    )
}

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
            .padding(16.dp),
        elevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
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
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            onViewReportClicked.invoke(report.pdfUrl)
                        }) {
                            Text(
                                text = stringResource(id = R.string.view_report)
                            )
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                            onDownloadPlayerReportClicked.invoke(report)
                        }) {
                            Text(
                                text = stringResource(id = R.string.download_report)
                            )
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            onDeletePlayerReportClicked.invoke(report)
                        }) {
                            Text(
                                text = stringResource(id = R.string.delete_report)
                            )
                        }
                    }
                }
            }
        }
    }
}

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
