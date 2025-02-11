package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
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
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogShotViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
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
            LogShotInfo(
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
            val player = getPlayer(
                isExisting = logShotViewModelExt.logShotInfo.isExistingPlayer,
                playerId = logShotViewModelExt.logShotInfo.playerId
            )

            currentPlayer = player
            currentPlayerShotSize = player?.shotsLoggedList?.size ?: 0
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
            updateViewShotState()
        }
    }

    private suspend fun updateViewShotState() {
        logShotViewModelExt.logShotInfo.let { info ->
            if (info.viewCurrentExistingShot) {
                updateShotStateFromExisting()
            }
            if (info.viewCurrentPendingShot) {
                updateShotStateFromPending()
            }
        }
        initializeShotLogged()
    }

    private fun updateShotStateFromExisting() {
        currentPlayer?.shotsLoggedList?.firstOrNull { it.id == logShotViewModelExt.logShotInfo.shotId }?.let { shot ->
            logShotMutableStateFlow.update { state ->
                state.copy(
                    shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                    shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                    shotsMade = shot.shotsMade,
                    shotsMissed = shot.shotsMissed,
                    shotsAttempted = shot.shotsAttempted,
                    shotsMadePercentValue = calculateShotPercentage(shot, isShotsMade = true),
                    shotsMissedPercentValue = calculateShotPercentage(shot, isShotsMade = false),
                    deleteShotButtonVisible = true
                )
            }
        }
    }

    private suspend fun updateShotStateFromPending() {
        currentPendingShot.shotsStateFlow.firstOrNull()?.firstOrNull()?.shotLogged?.let { shot ->
            logShotMutableStateFlow.update { state ->
                state.copy(
                    shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                    shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                    shotsMade = shot.shotsMade,
                    shotsMissed = shot.shotsMissed,
                    shotsAttempted = shot.shotsAttempted,
                    shotsMadePercentValue = calculateShotPercentage(shot, isShotsMade = true),
                    shotsMissedPercentValue = calculateShotPercentage(shot, isShotsMade = false),
                    deleteShotButtonVisible = false
                )
            }
        }
    }

    private fun calculateShotPercentage(shot: ShotLogged, isShotsMade: Boolean): String {
        return logShotViewModelExt.percentageFormat(
            shotsMade = shot.shotsMade.toDouble(),
            shotsMissed = shot.shotsMissed.toDouble(),
            isShotsMade = isShotsMade
        )
    }

    private fun initializeShotLogged() {
        initialShotLogged = ShotLogged(
            id = 0, // Ignored field
            shotName = logShotMutableStateFlow.value.shotName,
            shotType = currentDeclaredShot?.id ?: 0,
            shotsAttempted = logShotMutableStateFlow.value.shotsAttempted,
            shotsMade = logShotMutableStateFlow.value.shotsMade,
            shotsMissed = logShotMutableStateFlow.value.shotsMissed,
            shotsMadePercentValue = logShotViewModelExt.convertPercentageToDouble(logShotMutableStateFlow.value.shotsMadePercentValue.trim().replace(" ", "")),
            shotsMissedPercentValue = logShotViewModelExt.convertPercentageToDouble(logShotMutableStateFlow.value.shotsMissedPercentValue.trim().replace(" ", "")),
            shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(logShotMutableStateFlow.value.shotsTakenDateValue)?.time ?: 0L,
            shotsLoggedMillisecondsValue = logShotViewModelExt.convertValueToDate(logShotMutableStateFlow.value.shotsLoggedDateValue)?.time ?: 0L,
            isPending = true // Ignored field
        )
    }

    private suspend fun getPlayer(isExisting: Boolean, playerId: Int): Player? {
        return if (isExisting) {
            playerRepository.fetchPlayerById(id = playerId)
        } else {
            pendingPlayerRepository.fetchPlayerById(id = playerId)
        }
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

    private suspend fun handleFromShotListSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: updateCurrentShot(pendingShot = pendingShot)
    }

    fun onSaveClicked() {
        scope.launch {
            val player = currentPlayer
            if (player == null) {
                navigation.alert(
                    alert = logShotViewModelExt.invalidLogShotAlert(
                        description = application.getString(StringsIds.playerIsInvalidPleaseTryAgain)
                    )
                )
                return@launch
            }

            navigation.enableProgress(Progress())

            val state = logShotMutableStateFlow.value
            val shotDateMillis = logShotViewModelExt.convertValueToDate(value = state.shotsTakenDateValue)?.time ?: 0

            logShotViewModelExt.shotEntryInvalidAlert(
                shotsMade = state.shotsMade,
                shotsMissed = state.shotsMissed,
                shotsAttemptedMillisecondsValue = shotDateMillis
            )?.let { alert ->
                disableProgressAndShowAlert(alert = alert)
                return@launch
            }

            val pendingShot = buildPendingShotOnSave(player = player, state = state)
            val shotInfo = logShotViewModelExt.logShotInfo

            when {
                shotInfo.viewCurrentExistingShot && shotInfo.fromShotList -> handleFromShotListSaveClicked(pendingShot)
                shotInfo.viewCurrentPendingShot -> handlePendingShotSaveClicked(pendingShot)
                shotInfo.viewCurrentExistingShot -> handleExistingShotSaveClicked(pendingShot)
                else -> createPendingShot(isACurrentPlayerShot = false, pendingShot = pendingShot)
            }
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

    private suspend fun updateUserInFirebase(player: Player, shotLogged: List<ShotLogged>) {
        val key = activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey ?: ""
        val playerKey =
            safeLet(player.firstName, player.lastName) { firstName, lastName ->
                playerRepository.fetchPlayerByName(
                    firstName = firstName,
                    lastName = lastName
                )?.firebaseKey ?: ""
            } ?: run { "" }
        if (key.isNotEmpty() && playerKey.isNotEmpty()) {
            updateFirebaseUserInfo.updatePlayer(
                playerInfoRealtimeWithKeyResponse = PlayerInfoRealtimeWithKeyResponse(
                    playerFirebaseKey = playerKey,
                    playerInfo = PlayerInfoRealtimeResponse(
                        firstName = player.firstName,
                        lastName = player.lastName,
                        positionValue = player.position.value,
                        imageUrl = player.imageUrl ?: "",
                        shotsLogged = currentShotLoggedRealtimeResponseList(currentShotList = shotLogged)
                    )
                )
            ).collectLatest { isSuccessful ->
                handleUpdatedShot(
                    isSuccessful = isSuccessful,
                    player = player,
                    shotLogged = shotLogged
                )
            }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    internal fun currentShotLoggedRealtimeResponseList(currentShotList: List<ShotLogged>): List<ShotLoggedRealtimeResponse> {
        if (currentShotList.isNotEmpty()) {
            val shotLoggedRealtimeResponseArrayList: ArrayList<ShotLoggedRealtimeResponse> = arrayListOf()

            currentShotList.forEach { shotLogged ->
                shotLoggedRealtimeResponseArrayList.add(
                    ShotLoggedRealtimeResponse(
                        id = shotLogged.id,
                        shotName = shotLogged.shotName,
                        shotType = shotLogged.shotType,
                        shotsAttempted = shotLogged.shotsAttempted,
                        shotsMade = shotLogged.shotsMade,
                        shotsMissed = shotLogged.shotsMissed,
                        shotsMadePercentValue = shotLogged.shotsMadePercentValue,
                        shotsMissedPercentValue = shotLogged.shotsMissedPercentValue,
                        shotsAttemptedMillisecondsValue = shotLogged.shotsAttemptedMillisecondsValue,
                        shotsLoggedMillisecondsValue = shotLogged.shotsLoggedMillisecondsValue,
                        isPending = false
                    )
                )
            }
            return shotLoggedRealtimeResponseArrayList
        } else {
            return arrayListOf()
        }
    }

    private suspend fun handleUpdatedShot(
        isSuccessful: Boolean,
        player: Player,
        shotLogged: List<ShotLogged>
    ) {
        if (isSuccessful) {
            playerRepository.updatePlayer(
                currentPlayer = player,
                newPlayer = player.copy(
                    firstName = player.firstName,
                    lastName = player.lastName,
                    position = player.position,
                    firebaseKey = player.firebaseKey,
                    imageUrl = player.imageUrl,
                    shotsLoggedList = shotLogged
                )
            )
            dataAdditionUpdates.updateShotHasBeenUpdatedSharedFlow(hasShotBeenUpdated = true)

            navigation.disableProgress()
            navigation.popToShotList()
            navigation.alert(alert = logShotViewModelExt.showUpdatedAlert())
        } else {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    private suspend fun updateCurrentShot(pendingShot: PendingShot) {
        currentPlayer?.let { player ->
            updateUserInFirebase(
                player = player,
                shotLogged = logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) + listOf(pendingShot.shotLogged)
            )
        } ?: run {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    internal fun createPendingShot(isACurrentPlayerShot: Boolean, pendingShot: PendingShot) {
        currentPendingShot.createShot(
            shotLogged = if (isACurrentPlayerShot) {
                pendingShot
            } else {
                pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = currentPlayerShotSize + 1))
            }
        )
        navigateToCreateOrEditPlayer()
    }

    suspend fun handleHasDeleteShotFirebaseResponse(hasDeleted: Boolean) {
        val shotName = logShotMutableStateFlow.value.shotName

        if (hasDeleted) {
            if (logShotViewModelExt.logShotInfo.fromShotList) {
                dataAdditionUpdates.updateShotHasBeenUpdatedSharedFlow(hasShotBeenUpdated = true)
                navigation.disableProgress()
                navigation.popToShotList()
            } else {
                navigateToCreateOrEditPlayer()
            }
            navigation.alert(logShotViewModelExt.deleteShotConfirmAlert(shotName))
        } else {
            navigation.disableProgress()
            navigation.alert(logShotViewModelExt.deleteShotErrorAlert(shotName))
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
