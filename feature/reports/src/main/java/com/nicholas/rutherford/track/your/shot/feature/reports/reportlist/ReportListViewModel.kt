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
/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel for managing the report list screen. This ViewModel is responsible for
 * retrieving and updating the list of individual player reports, handling navigation,
 * displaying alerts, and performing operations like viewing, deleting, or downloading reports.
 *
 * @param application Application context used for accessing resources.
 * @param navigation Handles navigation actions from the report list screen.
 * @param playerRepository Repository for accessing player data.
 * @param individualPlayerReportRepository Repository for managing individual player reports.
 * @param deleteFirebaseUserInfo Handles deletion of reports from Firebase.
 * @param pdfGenerator Utility for downloading reports as PDFs.
 * @param scope Coroutine scope for launching asynchronous tasks.
 */
class ReportListViewModel(
    private val application: Application,
    private val navigation: ReportListNavigation,
    private val playerRepository: PlayerRepository,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val pdfGenerator: PdfGenerator,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal val reportListMutableStateFlow = MutableStateFlow(value = ReportListState())
    val reportListStateFlow = reportListMutableStateFlow.asStateFlow()

    init {
        updateReportListState()
    }

    /**
     * Handles the toolbar menu click by opening the navigation drawer.
     */
    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    /**
     * Updates the UI state if a report has been added.
     */
    fun handleReportAdded(hasBeenAdded: Boolean) {
        if (hasBeenAdded) {
            updateReportListState()
        }
    }

    /**
     * Fetches the latest list of reports and players, updating the screen state accordingly.
     */
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

    /**
     * Builds an alert informing the user no players have been added yet.
     */
    fun noPlayersAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noPlayersCreated),
            description = application.getString(StringsIds.noPlayersCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Builds an alert informing the user no shots have been logged yet.
     */
    fun noShotsAddedForPlayersAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noShotsCreated),
            description = application.getString(StringsIds.noShotsCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    /**
     * Called when the user attempts to create a new report. Displays alerts if there are
     * no players or no shots logged. Otherwise, navigates to the report creation screen.
     */
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

    /**
     * Handles deletion of a report both in Firebase and local storage.
     *
     * @param reportKey Firebase key of the report to delete.
     */
    private suspend fun onYesDeletePlayerReportClicked(reportKey: String) {
        navigation.enableProgress(progress = Progress())
        deleteFirebaseUserInfo.deleteReport(reportKey = reportKey)
            .collectLatest { isSuccessful ->
                if (isSuccessful) {
                    navigation.disableProgress()
                    individualPlayerReportRepository.deleteReportByFirebaseKey(firebaseKey = reportKey)
                    updateReportListState()
                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = couldNotDeleteReportAlert())
                }
            }
    }

    /**
     * Builds a confirmation alert for deleting a report.
     *
     * @param individualPlayerReport The report to be deleted.
     */
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

    /**
     * Builds an error alert in case the report could not be deleted.
     */
    private fun couldNotDeleteReportAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotDeleteReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotDeleteReportDescription)
        )
    }

    /**
     * Converts a timestamp to a human-readable date string.
     *
     * @param value Time in milliseconds.
     */
    fun buildDateTimeStamp(value: Long): String = Date(value).toTimestampString()

    /**
     * Navigates to a web browser to view the given report PDF.
     *
     * @param url PDF URL to open.
     */
    fun onViewReportClicked(url: String) = navigation.navigateToUrl(url = url)

    /**
     * Shows the confirmation alert to delete a report.
     *
     * @param individualPlayerReport The report to be deleted.
     */
    fun onDeletePlayerReportClicked(individualPlayerReport: IndividualPlayerReport) =
        navigation.alert(alert = deleteReportAlert(individualPlayerReport = individualPlayerReport))

    /**
     * Initiates downloading the given report as a PDF.
     *
     * @param individualPlayerReport The report whose PDF should be downloaded.
     */
    fun onDownloadPlayerReportClicked(individualPlayerReport: IndividualPlayerReport) =
        pdfGenerator.downloadPdf(url = individualPlayerReport.pdfUrl)
}
