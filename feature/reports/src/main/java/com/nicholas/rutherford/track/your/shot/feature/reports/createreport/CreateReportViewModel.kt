package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.buildPlayersWithShots
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.room.response.sortedPlayers
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
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
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
) : ViewModel() {

    internal val createReportMutableStateFlow = MutableStateFlow(value = CreateReportState())
    val createReportStateFlow = createReportMutableStateFlow.asStateFlow()

    fun onToolbarMenuClicked() = navigation.pop()

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

    fun createPdfErrorAlert(statusCode: Int): Alert {
        return if (statusCode == Constants.PDF_CANNOT_CREATE_PDF_CODE) {
            cannotCreatePdfAlert()
        } else {
            cannotSavePdfAlert()
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
                    createFirebaseUserInfo.attemptToCreatePdfFirebaseStorageResponseFlow(uri = uri)
                        .collectLatest {
                            println("here is the url $it")
                        }
                }
                navigation.disableProgress()

                navigation.alert(alert = reportGeneratedForPlayer(playerName = fullName))
                navigation.pop()
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = createPdfErrorAlert(statusCode = pdf.second))
            }
        }
    }
}