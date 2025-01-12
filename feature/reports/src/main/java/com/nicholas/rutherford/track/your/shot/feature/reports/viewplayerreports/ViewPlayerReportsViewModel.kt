package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewPlayerReportsViewModel(
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val navigation: ViewPlayerReportsNavigation,
    private val accountManager: AccountManager,
    private val application: Application,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val pdfGenerator: PdfGenerator,
    private val scope: CoroutineScope
) : ViewModel() {

    val viewPlayerReportsMutableStateFlow = MutableStateFlow(value = ViewPlayerReportsState())
    val viewPlayerReportsStateFlow = viewPlayerReportsMutableStateFlow.asStateFlow()

    init {
        updateViewPlayerReportsState()
        collectLoggedInIndividualPlayerReportListStateFlow()
        collectNewReportHasBeenAddedSharedFlow()
    }

    fun onToolbarMenuClicked() = popAndResetState()

    fun collectLoggedInIndividualPlayerReportListStateFlow() {
        scope.launch {
            accountManager.loggedInIndividualPlayerReportListStateFlow.collectLatest { reports ->
                handleLoggedInReportsStateFlow(reports = reports)
            }
        }
    }

    fun collectNewReportHasBeenAddedSharedFlow() {
        scope.launch {
            dataAdditionUpdates.newReportHasBeenAddedSharedFlow.collectLatest { hasBeenAdded ->
                handleReportAdded(hasBeenAdded = hasBeenAdded)
            }
        }
    }

    fun handleLoggedInReportsStateFlow(reports: List<IndividualPlayerReport>) {
        if (reports.isNotEmpty()) {
            viewPlayerReportsMutableStateFlow.update { state -> state.copy(reports = reports) }
        }
    }

    fun handleReportAdded(hasBeenAdded: Boolean) {
        if (hasBeenAdded) {
            updateViewPlayerReportsState()
        }
    }

    fun popAndResetState() {
        navigation.pop()
        viewPlayerReportsMutableStateFlow.update { state -> state.copy(reports = emptyList()) }
    }

    fun updateViewPlayerReportsState(shouldCheckForPop: Boolean = false) {
        scope.launch {
            val reports = individualPlayerReportRepository.fetchAllReports()

            viewPlayerReportsMutableStateFlow.update { state -> state.copy(reports = reports) }

            if (shouldCheckForPop && reports.isEmpty()) {
                popAndResetState()
            }
        }
    }

    private suspend fun onYesDeletePlayerClicked(reportKey: String) {
        navigation.enableProgress(progress = Progress())
        deleteFirebaseUserInfo.deleteReport(reportKey = reportKey)
            .collectLatest { isSuccessful ->
                if (isSuccessful) {
                    navigation.disableProgress()
                    individualPlayerReportRepository.deleteReportByFirebaseKey(firebaseKey = reportKey)
                    updateViewPlayerReportsState(shouldCheckForPop = true)
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
                onButtonClicked = { scope.launch { onYesDeletePlayerClicked(reportKey = individualPlayerReport.firebaseKey) } }
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

    fun onDeletePlayerReportClicked(individualPlayerReport: IndividualPlayerReport) = navigation.alert(alert = deleteReportAlert(individualPlayerReport = individualPlayerReport))

    fun onDownloadPlayerReportClicked(individualPlayerReport: IndividualPlayerReport) =
        pdfGenerator.downloadPdf(url = individualPlayerReport.pdfUrl)
}
