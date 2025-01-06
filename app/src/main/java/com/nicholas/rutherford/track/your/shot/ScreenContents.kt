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
                permissionNotGrantedForReadMediaOrExternalStorageAlert = { createEditPlayerViewModel.permissionNotGrantedForReadMediaOrExternalStorageAlert() },
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
                onToolbarMenuClicked = { reportListViewModel.onToolbarMenuClicked() },
                onHelpClicked = { reportListViewModel.onHelpClicked() },
                onReportItemClicked = { index -> reportListViewModel.onReportItemClicked(index = index) },
                state = reportListViewModel.reportListStateFlow.collectAsState().value
            )
        )
    }

    fun createReportContent(createReportViewModel: CreateReportViewModel, shouldRefreshData: Boolean): @Composable () -> Unit = {
        CreateReportScreen(params = CreateReportParams(
            onToolbarMenuClicked = { createReportViewModel.onToolbarMenuClicked() },
            updatePlayersState = { createReportViewModel.updatePlayersState() },
            attemptToGeneratePlayerReport = { createReportViewModel.attemptToGeneratePlayerReport() },
            state = createReportViewModel.createReportStateFlow.collectAsState().value,
            shouldRefreshData = shouldRefreshData
        ))
    }
}
