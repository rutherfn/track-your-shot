package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import android.app.Application
import android.net.Uri
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.buildPlayersWithShots
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.room.response.sortedPlayers
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGenerator
import com.nicholas.rutherford.track.your.shot.notifications.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the Create Report screen.
 *
 * Handles player selection, PDF generation, uploading reports to Firebase,
 * and notifying the UI about progress and alerts.
 *
 * @param application Application context for accessing resources.
 * @param navigation Navigation handler for screen and alert interactions.
 * @param playerRepository Repository for fetching player data.
 * @param scope Coroutine scope for asynchronous operations.
 * @param notifications Handles building user notifications related to report creation.
 * @param pdfGenerator Utility to generate player shot reports as PDFs.
 * @param createFirebaseUserInfo Handles Firebase interactions for storing report data.
 * @param individualPlayerReportRepository Repository for saving report data locally.
 * @param dataAdditionUpdates Shared flow to notify other components about new report additions.
 * @param dateExt Utility for fetching the current date/time.
 */
class CreateReportViewModel(
    private val application: Application,
    private val navigation: CreateReportNavigation,
    private val playerRepository: PlayerRepository,
    private val scope: CoroutineScope,
    private val notifications: Notifications,
    private val pdfGenerator: PdfGenerator,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val dateExt: DateExt
) : BaseViewModel() {

    internal val createReportMutableStateFlow = MutableStateFlow(value = CreateReportState())
    val createReportStateFlow = createReportMutableStateFlow.asStateFlow()

    init {
        updatePlayersState()
    }

    /**
     * Resets the UI state to default.
     */
    fun resetState() {
        createReportMutableStateFlow.value = CreateReportState()
    }

    /**
     * Handles toolbar menu click by navigating back.
     */
    fun onToolbarMenuClicked() = navigation.pop()

    /**
     * Fetches all players, sorts them, and updates the UI state with player options.
     * Selects the first player by default if available.
     */
    fun updatePlayersState() {
        scope.launch {
            val sortedPlayers = playerRepository.fetchAllPlayers().buildPlayersWithShots().sortedPlayers()
            val playerOptions = sortedPlayers.map { it.fullName() }

            if (playerOptions.isNotEmpty()) {
                createReportMutableStateFlow.update { state ->
                    state.copy(playerOptions = playerOptions, selectedPlayer = sortedPlayers.first())
                }
            }
        }
    }

    /**
     * Creates an alert to show when the PDF report cannot be created.
     */
    fun cannotCreatePdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotCreateReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotGenerateTheReport)
        )
    }

    /**
     * Creates an alert to show when the PDF report cannot be saved.
     */
    fun cannotSavePdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotSaveReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotGenerateTheReport)
        )
    }

    /**
     * Creates an alert indicating a player report was successfully generated.
     *
     * @param playerName The name of the player the report was generated for.
     */
    fun reportGeneratedForPlayer(playerName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.playerReportCreatedForX, playerName),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playerReportCreatedDescription)
        )
    }

    /**
     * Creates an alert to show when the PDF report upload to Firebase fails.
     */
    fun cannotUploadPdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotUploadReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotUploadReportDescription)
        )
    }

    /**
     * Returns an appropriate alert based on the PDF generation error code.
     *
     * @param statusCode The error code returned during PDF generation.
     */
    fun createPdfErrorAlert(statusCode: Int): Alert {
        return if (statusCode == Constants.PDF_CANNOT_CREATE_PDF_CODE) {
            cannotCreatePdfAlert()
        } else {
            cannotSavePdfAlert()
        }
    }

    /**
     * Stores the generated report data locally and notifies the UI and other components.
     *
     * @param currentDateTime Timestamp of the report generation.
     * @param playerName Name of the player for whom the report was created.
     * @param reportKey Firebase key of the uploaded report.
     * @param pdfUrl URL of the uploaded PDF report.
     */
    suspend fun storeReport(
        currentDateTime: Long,
        playerName: String,
        reportKey: String,
        pdfUrl: String
    ) {
        individualPlayerReportRepository.createReport(
            report = IndividualPlayerReport(
                id = individualPlayerReportRepository.fetchReportCount() + 1,
                loggedDateValue = currentDateTime,
                playerName = playerName,
                firebaseKey = reportKey,
                pdfUrl = pdfUrl
            )
        )
        navigation.disableProgress()
        dataAdditionUpdates.updateNewReportHasBeenAddedSharedFlow(hasBeenAdded = true)

        navigation.alert(alert = reportGeneratedForPlayer(playerName = playerName))
        resetState()
        navigation.pop()
    }

    /**
     * Updates the selected player in the UI state based on the player name.
     *
     * @param playerName The full name of the player selected.
     */
    fun onPlayerChanged(playerName: String) {
        scope.launch {
            createReportMutableStateFlow.update { state -> state.copy(selectedPlayer = buildSelectedPlayer(value = playerName)) }
        }
    }

    /**
     * Returns back selected [Player] based on if it matches the [value] passed in
     *
     * @param value player full name to compare in the database which [Player] we should be returning
     */
    private suspend fun buildSelectedPlayer(value: String): Player? {
        var selectedPlayer: Player? = null
        playerRepository.fetchAllPlayers().buildPlayersWithShots().sortedPlayers().forEach { player ->
            if (player.fullName() == value) {
                selectedPlayer = player
            }
        }

        return selectedPlayer
    }

    /**
     * Attempts to upload the generated PDF report to Firebase storage and
     * save the associated metadata in Firebase Realtime Database.
     * Shows alerts and updates UI accordingly based on success or failure.
     *
     * @param uri The URI of the generated PDF report.
     * @param fullName The full name of the player associated with the report.
     */
    fun attemptToUploadAndSaveReport(uri: Uri, fullName: String) {
        val currentDateTime = dateExt.now

        scope.launch {
            createFirebaseUserInfo.attemptToCreatePdfFirebaseStorageResponseFlow(uri = uri)
                .collectLatest {
                    it?.let { pdfUrl ->
                        createFirebaseUserInfo.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(
                            individualPlayerReportRealtimeResponse = IndividualPlayerReportRealtimeResponse(
                                loggedDateValue = currentDateTime,
                                playerName = fullName,
                                pdfUrl = pdfUrl
                            )
                        ).collectLatest { result ->
                            if (result.first && !result.second.isNullOrEmpty()) {
                                storeReport(
                                    currentDateTime = currentDateTime,
                                    playerName = fullName,
                                    reportKey = result.second ?: "",
                                    pdfUrl = pdfUrl
                                )
                            } else {
                                navigation.disableProgress()
                                navigation.alert(alert = cannotUploadPdfAlert())
                            }
                        }
                    } ?: run {
                        navigation.disableProgress()
                        navigation.alert(alert = cannotUploadPdfAlert())
                    }
                }
        }
    }

    /**
     * Initiates the process to generate a player report PDF.
     * Displays progress, handles notification, and triggers upload.
     * Shows error alerts if PDF generation fails or if no player is selected.
     */
    fun attemptToGeneratePlayerReport() {
        navigation.enableProgress(progress = Progress())
        createReportMutableStateFlow.value.selectedPlayer?.let { player ->
            val fullName = player.fullName()

            val pdf = pdfGenerator.generatePlayerPdf(
                fileName = application.getString(StringsIds.xPlayerShotReport, "${player.firstName}${player.lastName}"),
                player = player
            )

            pdf.first?.let { uri ->
                notifications.buildPlayerReportNotification(
                    uri = uri,
                    title = application.getString(
                        StringsIds.xShotReportCreated,
                        fullName
                    ),
                    description = application.getString(
                        StringsIds.xShotReportCreatedDescription,
                        fullName
                    )
                )
                scope.launch {
                    attemptToUploadAndSaveReport(uri = uri, fullName = fullName)
                }
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = createPdfErrorAlert(statusCode = pdf.second))
            }
        } ?: run {
            navigation.disableProgress()
            navigation.alert(alert = createPdfErrorAlert(statusCode = Constants.PDF_CANNOT_CREATE_PDF_CODE))
        }
    }
}
