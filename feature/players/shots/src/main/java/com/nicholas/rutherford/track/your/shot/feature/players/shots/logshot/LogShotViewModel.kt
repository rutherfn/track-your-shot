package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotInfo
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExt
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogShotViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val currentPendingShot: CurrentPendingShot,
    private val logShotViewModelExt: LogShotViewModelExt
) : ViewModel() {

    val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    internal var currentPlayer: Player? = null
    internal var currentDeclaredShot: DeclaredShot? = null

    private var currentPlayerShotSize = 0

    internal var initialShotLogged: ShotLogged? = null

    fun updateIsExistingPlayerAndId(
        isExistingPlayerArgument: Boolean,
        playerIdArgument: Int,
        shotTypeArgument: Int,
        shotIdArgument: Int,
        viewCurrentExistingShotArgument: Boolean,
        viewCurrentPendingShotArgument: Boolean,
        fromShotListArgument: Boolean
    ) {
        resetState()
        logShotViewModelExt.setInitialInfo(
            logShotInfo = LogShotInfo(
                isExistingPlayer = isExistingPlayerArgument,
                playerId = playerIdArgument,
                shotType = shotTypeArgument,
                shotId = shotIdArgument,
                viewCurrentExistingShot = viewCurrentExistingShotArgument,
                viewCurrentPendingShot = viewCurrentPendingShotArgument,
                fromShotList = fromShotListArgument
            )
        )

        scope.launch {
            val declaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = logShotViewModelExt.logShotInfo.shotType)

            val player = if (logShotViewModelExt.logShotInfo.isExistingPlayer) {
                playerRepository.fetchPlayerById(id = logShotViewModelExt.logShotInfo.playerId)
            } else {
                pendingPlayerRepository.fetchPlayerById(id = logShotViewModelExt.logShotInfo.playerId)
            }

            currentPlayer = player
            currentPlayerShotSize = currentPlayer?.shotsLoggedList?.size ?: 0
            currentDeclaredShot = declaredShot

            safeLet(declaredShot, player) { shot, existingPlayer ->
                logShotMutableStateFlow.update { state ->
                    state.copy(
                        shotName = shot.title,
                        playerPosition = existingPlayer.position.toType(),
                        playerName = "${existingPlayer.firstName}, ${existingPlayer.lastName}",
                        shotsLoggedDateValue = LocalDate.now().toDateValue() ?: ""
                    )
                }
            }

            updateStateForViewShot()
        }
    }

    private suspend fun updateStateForViewShot() {
        if (logShotViewModelExt.logShotInfo.viewCurrentExistingShot) {
            currentPlayer?.shotsLoggedList?.first { shotLogged -> shotLogged.id == logShotViewModelExt.logShotInfo.shotId }?.let { shot ->
                logShotMutableStateFlow.update { state ->
                    state.copy(
                        shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                        shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                        shotsMade = shot.shotsMade,
                        shotsMissed = shot.shotsMissed,
                        shotsAttempted = shot.shotsAttempted,
                        shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = true
                        ),
                        shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = false
                        ),
                        deleteShotButtonVisible = true
                    )
                }
            }
        }

        if (logShotViewModelExt.logShotInfo.viewCurrentPendingShot) {
            val pendingShotList = currentPendingShot.shotsStateFlow.first()

            if (pendingShotList.isNotEmpty()) {
                val shot = pendingShotList.first().shotLogged

                logShotMutableStateFlow.update { state ->
                    state.copy(
                        shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                        shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                        shotsMade = shot.shotsMade,
                        shotsMissed = shot.shotsMissed,
                        shotsAttempted = shot.shotsAttempted,
                        shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = true
                        ),
                        shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = false
                        ),
                        deleteShotButtonVisible = false
                    )
                }
            }
        }

        initialShotLogged = ShotLogged(
            id = 0, // does not matter since were ignoring this field
            shotName = logShotMutableStateFlow.value.shotName,
            shotType = currentDeclaredShot?.id ?: 0,
            shotsAttempted = logShotMutableStateFlow.value.shotsAttempted,
            shotsMade = logShotMutableStateFlow.value.shotsMade,
            shotsMissed = logShotMutableStateFlow.value.shotsMissed,
            shotsMadePercentValue = logShotViewModelExt.convertPercentageToDouble(percentage = logShotMutableStateFlow.value.shotsMadePercentValue.trim().replace(" ", "")),
            shotsMissedPercentValue = logShotViewModelExt.convertPercentageToDouble(percentage = logShotMutableStateFlow.value.shotsMissedPercentValue.trim().replace(" ", "")),
            shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = logShotMutableStateFlow.value.shotsTakenDateValue)?.time
                ?: 0L,
            shotsLoggedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = logShotMutableStateFlow.value.shotsLoggedDateValue)?.time
                ?: 0L,
            isPending = true // does not matter since were ignoring this field
        )
    }

    fun onDateShotsTakenClicked() {
        val dateValue = logShotMutableStateFlow.value.shotsTakenDateValue

        val datePickerInfo = DatePickerInfo(
            onDateOkClicked = { newDateValue ->
                logShotMutableStateFlow.update { state ->
                    state.copy(shotsTakenDateValue = newDateValue)
                }
            },
            dateValue = dateValue
        )
        navigation.datePicker(datePickerInfo = datePickerInfo)
    }

    fun onShotsMadeUpwardOrDownwardClicked(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMade = shots,
                shotsAttempted = logShotViewModelExt.shotsAttempted(shotsMade = shots, shotsMissed = state.shotsMissed),
                shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    fun onShotsMissedUpwardOrDownwardClicked(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMissed = shots,
                shotsAttempted = logShotViewModelExt.shotsAttempted(shotsMade = state.shotsMade, shotsMissed = shots),
                shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    internal fun disableProgressAndShowAlert(alert: Alert) {
        navigation.disableProgress()
        navigation.alert(alert = alert)
    }

    private fun buildPendingShotOnSave(player: Player, state: LogShotState): PendingShot =
        PendingShot(
            player = player,
            shotLogged = ShotLogged(
                id = 0,
                shotName = state.shotName,
                shotType = currentDeclaredShot?.id ?: 0,
                shotsAttempted = state.shotsAttempted,
                shotsMade = state.shotsMade,
                shotsMissed = state.shotsMissed,
                shotsMadePercentValue = logShotViewModelExt.convertPercentageToDouble(
                    percentage = state.shotsMadePercentValue.trim().replace(" ", "")
                ),
                shotsMissedPercentValue = logShotViewModelExt.convertPercentageToDouble(
                    percentage = state.shotsMissedPercentValue.trim().replace(" ", "")
                ),
                shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = state.shotsTakenDateValue)?.time
                    ?: 0L,
                shotsLoggedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = state.shotsLoggedDateValue)?.time
                    ?: 0L,
                isPending = true
            ),
            isPendingPlayer = logShotViewModelExt.logShotInfo.isExistingPlayer
        )

    private fun handleExistingShotSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: createPendingShot(
            isACurrentPlayerShot = true,
            pendingShot = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = logShotViewModelExt.logShotInfo.shotId))
        )
    }

    private fun handlePendingShotSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: updatePendingShot(pendingShot = pendingShot)
    }

    fun onSaveClicked() {
        scope.launch {
            currentPlayer?.let { player ->
                navigation.enableProgress(Progress())
                val state = logShotMutableStateFlow.value

                logShotViewModelExt.shotEntryInvalidAlert(
                    shotsMade = state.shotsMade,
                    shotsMissed = state.shotsMissed,
                    shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = state.shotsTakenDateValue)?.time ?: 0
                )?.let { alert ->
                    disableProgressAndShowAlert(alert = alert)
                } ?: run {
                    if (logShotViewModelExt.logShotInfo.viewCurrentExistingShot) {
                        handleExistingShotSaveClicked(pendingShot = buildPendingShotOnSave(player = player, state = state))
                    } else if (logShotViewModelExt.logShotInfo.viewCurrentPendingShot) {
                        handlePendingShotSaveClicked(pendingShot = buildPendingShotOnSave(player = player, state = state))
                    } else {
                        createPendingShot(
                            isACurrentPlayerShot = false,
                            pendingShot = buildPendingShotOnSave(player = player, state = state)
                        )
                    }
                }
            } ?: navigation.alert(
                alert = logShotViewModelExt.invalidLogShotAlert(
                    description = application.getString(
                        StringsIds.playerIsInvalidPleaseTryAgain
                    )
                )
            )
        }
    }

    private fun resetState() =
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotName = "",
                playerName = "",
                playerPosition = 0,
                shotsLoggedDateValue = "",
                shotsTakenDateValue = "",
                shotsMade = 0,
                shotsMissed = 0,
                shotsAttempted = 0,
                shotsMadePercentValue = "",
                shotsMissedPercentValue = "",
                deleteShotButtonVisible = false
            )
        }

    private fun updatePendingShot(pendingShot: PendingShot) {
        val firstShotLogged = currentPendingShot.fetchPendingShots().first()
        currentPendingShot.deleteShot(shotLogged = firstShotLogged)
        currentPendingShot.createShot(shotLogged = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = firstShotLogged.shotLogged.id)))
        navigateToCreateOrEditPlayer()
    }

    internal fun createPendingShot(isACurrentPlayerShot: Boolean, pendingShot: PendingShot) {
        if (isACurrentPlayerShot) {
            currentPendingShot.createShot(shotLogged = pendingShot.copy(shotLogged = pendingShot.shotLogged))
        } else {
            currentPendingShot.createShot(shotLogged = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = currentPlayerShotSize + 1)))
        }
        navigateToCreateOrEditPlayer()
    }

    fun handleHasDeleteShotFirebaseResponse(hasDeleted: Boolean) {
        if (hasDeleted) {
            if (logShotViewModelExt.logShotInfo.fromShotList) {
                navigation.disableProgress()
                navigation.popToShotList()
            } else {
                navigateToCreateOrEditPlayer()
            }
            navigation.alert(alert = logShotViewModelExt.deleteShotConfirmAlert(shotName = logShotMutableStateFlow.value.shotName))
        } else {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.deleteShotErrorAlert(shotName = logShotMutableStateFlow.value.shotName))
        }
    }

    fun navigateToCreateOrEditPlayer() {
        navigation.disableProgress()
        if (logShotViewModelExt.logShotInfo.isExistingPlayer) {
            navigation.popToEditPlayer()
        } else {
            navigation.popToCreatePlayer()
        }
    }

    fun onBackClicked() = navigation.pop()

    suspend fun onYesDeleteShot() {
        navigation.enableProgress(progress = Progress())
        currentPlayer?.let { player ->
            playerRepository.updatePlayer(
                currentPlayer = player,
                newPlayer = player.copy(
                    firstName = player.firstName,
                    lastName = player.lastName,
                    position = player.position,
                    firebaseKey = player.firebaseKey,
                    imageUrl = player.imageUrl,
                    shotsLoggedList = logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList)
                )
            )

            deleteFirebaseUserInfo.deleteShot(
                playerKey = player.firebaseKey,
                index = logShotViewModelExt.logShotInfo.shotId - 1
            ).collectLatest { hasDeleted ->
                handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)
            }
        } ?: navigation.disableProgress()
    }

    fun onDeleteShotClicked() = navigation.alert(alert = logShotViewModelExt.deleteShotAlert(onYesDeleteShot = { onYesDeleteShot() }, shotName = logShotMutableStateFlow.value.shotName))
}
