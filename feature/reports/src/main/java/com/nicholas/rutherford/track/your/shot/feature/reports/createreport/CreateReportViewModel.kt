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

    val createReportMutableStateFlow = MutableStateFlow(value = CreateReportState())
    val createReportStateFlow = createReportMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updatePlayersState()
    }

    fun resetState() {
        createReportMutableStateFlow.value = CreateReportState()
    }

    fun onToolbarMenuClicked() {
        navigation.pop()
    }

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

    fun cannotCreatePdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotCreateReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotGenerateTheReport)
        )
    }

    fun cannotSavePdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotSaveReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotGenerateTheReport)
        )
    }

    fun reportGeneratedForPlayer(playerName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.playerReportCreatedForX, playerName),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playerReportCreatedDescription)
        )
    }

    fun cannotUploadPdfAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotUploadReport),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.couldNotUploadReportDescription)
        )
    }

    fun createPdfErrorAlert(statusCode: Int): Alert {
        return if (statusCode == Constants.PDF_CANNOT_CREATE_PDF_CODE) {
            cannotCreatePdfAlert()
        } else {
            cannotSavePdfAlert()
        }
    }

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

    fun onPlayerChanged(playerName: String) {
        scope.launch {
            createReportMutableStateFlow.update { state -> state.copy(selectedPlayer = buildSelectedPlayer(value = playerName)) }
        }
    }

    private suspend fun buildSelectedPlayer(value: String): Player? {
        var selectedPlayer: Player? = null
        playerRepository.fetchAllPlayers().buildPlayersWithShots().sortedPlayers().forEach { player ->
            if (player.fullName() == value) {
                selectedPlayer = player
            }
        }

        return selectedPlayer
    }

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
