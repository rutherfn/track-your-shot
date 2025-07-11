package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportParams
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportScreen
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListScreen

class ScreenContents {

    fun reportListContent(reportListParams: ReportListParams): @Composable () -> Unit = {
        ReportListScreen(
            params = reportListParams
        )
    }

    fun createReportContent(createReportViewModel: CreateReportViewModel): @Composable () -> Unit = {
        CreateReportScreen(
            params = CreateReportParams(
                onToolbarMenuClicked = { createReportViewModel.onToolbarMenuClicked() },
                onPlayerChanged = { playerName -> createReportViewModel.onPlayerChanged(playerName = playerName) },
                attemptToGeneratePlayerReport = { createReportViewModel.attemptToGeneratePlayerReport() },
                state = createReportViewModel.createReportStateFlow.collectAsState().value
            )
        )
    }
}
