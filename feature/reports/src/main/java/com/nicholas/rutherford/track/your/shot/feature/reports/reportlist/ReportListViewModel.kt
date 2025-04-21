package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.getAllShots
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.extensions.toTimestampString
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

// todo -> Unit test this in a follow up PR
class ReportListViewModel(
    private val application: Application,
    private val navigation: ReportListNavigation,
    private val playerRepository: PlayerRepository,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val pdfGenerator: PdfGenerator,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal val reportListMutableStateFlow = MutableStateFlow(value = ReportListState())
    val reportListStateFlow = reportListMutableStateFlow.asStateFlow()

    init {
        collectNewReportHasBeenAddedSharedFlow()
    }

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updateReportListState()
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun collectNewReportHasBeenAddedSharedFlow() {
        scope.launch {
            dataAdditionUpdates.newReportHasBeenAddedSharedFlow.collectLatest { hasBeenAdded ->
                handleReportAdded(hasBeenAdded = hasBeenAdded)
            }
        }
    }

    fun handleReportAdded(hasBeenAdded: Boolean) {
        if (hasBeenAdded) {
            updateReportListState()
        }
    }

    fun updateReportListState() {
        scope.launch {
            val reports = individualPlayerReportRepository.fetchAllReports()
            val players = playerRepository.fetchAllPlayers()

            val hasNoPermission = players.isEmpty() || players.getAllShots().isEmpty()

            reportListMutableStateFlow.update { state ->
                state.copy(
                    reports = reports,
                    hasNoReportPermission = hasNoPermission && reports.isEmpty(),
                    hasNoReports = reports.isEmpty()
                )
            }
        }
    }

    fun noPlayersAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noPlayersCreated),
            description = application.getString(StringsIds.noPlayersCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    fun noShotsAddedForPlayersAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noShotsCreated),
            description = application.getString(StringsIds.noShotsCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    fun onCreatePlayerReportClicked() {
        scope.launch {
            val players = playerRepository.fetchAllPlayers()

            if (players.isEmpty()) {
                navigation.alert(alert = noPlayersAddedAlert())
            } else if (players.getAllShots().isEmpty()) {
                navigation.alert(alert = noShotsAddedForPlayersAlert())
            } else {
                navigation.navigateToCreateReport()
            }
        }
    }

    private suspend fun onYesDeletePlayerReportClicked(reportKey: String) {
        navigation.enableProgress(progress = Progress())
        deleteFirebaseUserInfo.deleteReport(reportKey = reportKey)
            .collectLatest { isSuccessful ->
                if (isSuccessful) {
                    navigation.disableProgress()
                    navigation.disableProgress()
                    individualPlayerReportRepository.deleteReportByFirebaseKey(firebaseKey = reportKey)
                    updateReportListState()
                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = couldNotDeleteReportAlert())
                }
            }
    }

    private fun deleteReportAlert(individualPlayerReport: IndividualPlayerReport): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteReport),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeletePlayerReportClicked(reportKey = individualPlayerReport.firebaseKey) } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            ),
            description = application.getString(StringsIds.deleteReportDescription)
        )
    }

    private fun couldNotDeleteReportAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotDeleteReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            ),
            description = application.getString(StringsIds.couldNotDeleteReportDescription)
        )
    }

    fun buildDateTimeStamp(value: Long): String = Date(value).toTimestampString()

    fun onViewReportClicked(url: String) = navigation.navigateToUrl(url = url)

    fun onDeletePlayerReportClicked(individualPlayerReport: IndividualPlayerReport) = navigation.alert(alert = deleteReportAlert(individualPlayerReport = individualPlayerReport))

    fun onDownloadPlayerReportClicked(individualPlayerReport: IndividualPlayerReport) =
        pdfGenerator.downloadPdf(url = individualPlayerReport.pdfUrl)
}
