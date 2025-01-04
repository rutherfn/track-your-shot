package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun ReportListScreen(params: ReportListParams) {
    Content(
        ui = {
            ReportListContent(params = params)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.reports),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onHelpClicked.invoke() }
        ),
        secondaryImageVector = Icons.Filled.Help
    )
}

@Composable
fun ReportListContent(params: ReportListParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.eight))

        params.state.reportInfoList.forEachIndexed { index, reportTitle ->
            ReportRowCard(
                reportTitle = reportTitle,
                index = index,
                onReportItemClicked = params.onReportItemClicked
            )
        }
    }
}

@Composable
private fun ReportRowCard(
    reportTitle: String,
    index: Int,
    onReportItemClicked: (value: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(top = Padding.eight, bottom = Padding.eight),
        elevation = 2.dp
    ) {
        Column {
            BaseRow(
                title = reportTitle,
                titleStyle = TextStyles.bodyBold,
                onClicked = { onReportItemClicked.invoke(index) },
                imageVector = Icons.Filled.ChevronRight
            )
        }
    }
}
