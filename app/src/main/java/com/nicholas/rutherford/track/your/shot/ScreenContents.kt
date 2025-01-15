package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerScreen
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportParams
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportScreen
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListScreen
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListViewModel

class ScreenContents {

    fun createEditPlayerContent(
        firstNameArgument: String?,
        lastNameArgument: String?,
        createEditPlayerViewModel: CreateEditPlayerViewModel
    ): @Composable (NavBackStackEntry) -> Unit = {
        CreateEditPlayerScreen(
            createEditPlayerParams = CreateEditPlayerParams(
                state = createEditPlayerViewModel.createEditPlayerStateFlow.collectAsState().value,
                onClearImageState = { createEditPlayerViewModel.onClearImageState() },
                checkForExistingPlayer = {
                    createEditPlayerViewModel.checkForExistingPlayer(
                        firstNameArgument = firstNameArgument,
                        lastNameArgument = lastNameArgument
                    )
                },
                onToolbarMenuClicked = { createEditPlayerViewModel.onToolbarMenuClicked() },
                onLogShotsClicked = { createEditPlayerViewModel.onLogShotsClicked() },
                onFirstNameValueChanged = { newFirstName ->
                    createEditPlayerViewModel.onFirstNameValueChanged(
                        newFirstName = newFirstName
                    )
                },
                onLastNameValueChanged = { newLastName ->
                    createEditPlayerViewModel.onLastNameValueChanged(
                        newLastName = newLastName
                    )
                },
                onPlayerPositionStringChanged = { newPositionStringResId ->
                    createEditPlayerViewModel.onPlayerPositionStringChanged(
                        newPositionStringResId
                    )
                },
                onImageUploadClicked = { uri -> createEditPlayerViewModel.onImageUploadClicked(uri) },
                onCreatePlayerClicked = { uri -> createEditPlayerViewModel.onCreatePlayerClicked(uri) },
                permissionNotGrantedForCameraAlert = { createEditPlayerViewModel.permissionNotGrantedForCameraAlert() },
                onSelectedCreateEditImageOption = { option ->
                    createEditPlayerViewModel.onSelectedCreateEditImageOption(option)
                },
                onViewShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewShotClicked(
                        shotType = shotType,
                        shotId = shotId
                    )
                },
                onViewPendingShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewPendingShotClicked(
                        shotType = shotType,
                        shotId = shotId
                    )
                }
            )
        )
    }

    fun reportListContent(reportListViewModel: ReportListViewModel): @Composable () -> Unit = {
        ReportListScreen(
            params = ReportListParams(
                state = reportListViewModel.reportListStateFlow.collectAsState().value,
                onToolbarMenuClicked = { reportListViewModel.onToolbarMenuClicked() },
                onAddReportClicked = { reportListViewModel.onCreatePlayerReportClicked() },
                onDeletePlayerReportClicked = { individualPlayerReport -> reportListViewModel.onDeletePlayerReportClicked(individualPlayerReport = individualPlayerReport) },
                onDownloadPlayerReportClicked = { individualPlayerReport -> reportListViewModel.onDownloadPlayerReportClicked(individualPlayerReport = individualPlayerReport) },
                buildDateTimeStamp = { value -> reportListViewModel.buildDateTimeStamp(value) }
            )
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
