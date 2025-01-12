package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun ViewPlayerReportsScreen(params: ViewPlayerReportsParams) {
    Content(
        ui = {
            LazyColumn {
                items(params.state.reports) { report ->
                    PlayerReport(
                        report = report,
                        onDeletePlayerReportClicked = params.onDeletePlayerReportClicked,
                        onDownloadPlayerReportClicked = params.onDownloadPlayerReportClicked
                    )
                }
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.viewReports),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }
        )
    )
}

@Composable
fun PlayerReport(
    report: IndividualPlayerReport,
    onDeletePlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit,
    onDownloadPlayerReportClicked: (individualPlayerReport: IndividualPlayerReport) -> Unit
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
                Text(
                    text = report.playerName,
                    style = TextStyles.bodyBold,
                    textAlign = TextAlign.Start
                )

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
