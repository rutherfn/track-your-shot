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
import com.nicholas.rutherford.track.your.shot.data.room.response.isTheSame
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseValueToDate
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class LogShotViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository,
    private val currentPendingShot: CurrentPendingShot
) : ViewModel() {

    internal val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    internal var isExistingPlayer = false
    private var playerId = 0
    internal var shotType = 0
    internal var shotId = 0

    internal var currentPlayer: Player? = null
    internal var currentDeclaredShot: DeclaredShot? = null

    private var currentPlayerShotSize = 0
    internal var viewCurrentExistingShot = false
    internal var viewCurrentPendingShot = false

    internal var initialShotLogged: ShotLogged? = null
    fun updateIsExistingPlayerAndId(
        isExistingPlayerArgument: Boolean,
        playerIdArgument: Int,
        shotTypeArgument: Int,
        shotIdArgument: Int,
        viewCurrentExistingShotArgument: Boolean,
        viewCurrentPendingShotArgument: Boolean
    ) {
        resetState()

        this.isExistingPlayer = isExistingPlayerArgument
        this.playerId = playerIdArgument
        this.shotType = shotTypeArgument
        this.shotId = shotIdArgument
        this.viewCurrentExistingShot = viewCurrentExistingShotArgument
        this.viewCurrentPendingShot = viewCurrentPendingShotArgument

        scope.launch {
            val declaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = shotType)

            val player = if (isExistingPlayer) {
                playerRepository.fetchPlayerById(id = playerId)
            } else {
                pendingPlayerRepository.fetchPlayerById(id = playerId)
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
        if (viewCurrentExistingShot) {
            currentPlayer?.shotsLoggedList?.first { shotLogged -> shotLogged.id == shotId }?.let { shot ->
                logShotMutableStateFlow.update { state ->
                    state.copy(
                        shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                        shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                        shotsMade = shot.shotsMade,
                        shotsMissed = shot.shotsMissed,
                        shotsAttempted = shot.shotsAttempted,
                        shotsMadePercentValue = percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = true
                        ),
                        shotsMissedPercentValue = percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = false
                        ),
                        deleteShotButtonVisible = true
                    )
                }
            }
        }

        if (viewCurrentPendingShot) {
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
                        shotsMadePercentValue = percentageFormat(
                            shotsMade = shot.shotsMade.toDouble(),
                            shotsMissed = shot.shotsMissed.toDouble(),
                            isShotsMade = true
                        ),
                        shotsMissedPercentValue = percentageFormat(
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
            shotsMadePercentValue = convertPercentageToDouble(percentage = logShotMutableStateFlow.value.shotsMadePercentValue),
            shotsMissedPercentValue = convertPercentageToDouble(percentage = logShotMutableStateFlow.value.shotsMissedPercentValue),
            shotsAttemptedMillisecondsValue = convertValueToDate(value = logShotMutableStateFlow.value.shotsTakenDateValue)?.time
                ?: 0L,
            shotsLoggedMillisecondsValue = convertValueToDate(value = logShotMutableStateFlow.value.shotsLoggedDateValue)?.time
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

    internal fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int {
        return if (shotsMade != 0 || shotsMissed != 0) {
            shotsMade + shotsMissed
        } else {
            0
        }
    }

    internal fun shotsPercentValue(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): Double {
        val totalShots = if (isShotsMade) {
            shotsMade + shotsMissed
        } else {
            shotsMissed + shotsMade
        }
        val percentValue =
            if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
                if (isShotsMade) {
                    shotsMade / totalShots * 100
                } else {
                    shotsMissed / totalShots * 100
                }
            } else {
                Constants.SHOT_ZERO_VALUE
            }

        return percentValue
    }

    internal fun percentageFormat(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): String {
        val percentage = shotsPercentValue(
            shotsMade = shotsMade,
            shotsMissed = shotsMissed,
            isShotsMade = isShotsMade
        )

        return if (percentage == Constants.SHOT_ZERO_VALUE) {
            application.getString(StringsIds.empty)
        } else {
            val locale = Locale("en", "US")
            val percentageRoundedValue = String.format(locale, "%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(
                    StringsIds.shotPercentage,
                    percentageRoundedValue.substring(0, percentageRoundedValue.length - 2)
                )
            } else {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue)
            }
        }
    }

    internal fun updateStateAfterShotsMadeInput(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMade = shots,
                shotsAttempted = shotsAttempted(shotsMade = shots, shotsMissed = state.shotsMissed),
                shotsMadePercentValue = percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    private fun updateShotsMissedState(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMissed = shots,
                shotsAttempted = shotsAttempted(shotsMade = state.shotsMade, shotsMissed = shots),
                shotsMadePercentValue = percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    internal fun startingInputAmount(amount: Int): Int? {
        return if (amount > 0) {
            amount
        } else {
            null
        }
    }

    fun onShotsMadeClicked() {
        navigation.inputInfo(
            inputInfo = InputInfo(
                titleResId = StringsIds.enterShotsMade,
                confirmButtonResId = StringsIds.ok,
                dismissButtonResId = StringsIds.cancel,
                placeholderResId = StringsIds.shotsMade,
                startingInputAmount = startingInputAmount(amount = logShotMutableStateFlow.value.shotsMade),
                onConfirmButtonClicked = { shots ->
                    if (shots.isNotEmpty()) {
                        updateStateAfterShotsMadeInput(shots = shots.toInt())
                    }
                }
            )
        )
    }

    fun onShotsMissedClicked() {
        navigation.inputInfo(
            inputInfo = InputInfo(
                titleResId = StringsIds.enterShotsMissed,
                confirmButtonResId = StringsIds.ok,
                dismissButtonResId = StringsIds.cancel,
                placeholderResId = StringsIds.shotsMissed,
                startingInputAmount = startingInputAmount(amount = logShotMutableStateFlow.value.shotsMissed),
                onConfirmButtonClicked = { shots ->
                    if (shots.isNotEmpty()) {
                        updateShotsMissedState(shots = shots.toInt())
                    }
                }
            )
        )
    }

    fun invalidLogShotAlert(description: String): Alert {
        return Alert(
            title = application.getString(StringsIds.empty),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            ),
            description = description
        )
    }

    internal fun shotEntryInvalidAlert(shotsMade: Int, shotsMissed: Int, shotsAttemptedMillisecondsValue: Long): Alert? {
        val description: String? = if (shotsMade == 0) {
            application.getString(StringsIds.shotsNotRecordedDescription)
        } else if (shotsMissed == 0) {
            application.getString(StringsIds.missedShotsNotRecordedDescription)
        } else if (shotsAttemptedMillisecondsValue == 0L) {
            application.getString(StringsIds.dateShotWasTakenDescription)
        } else {
            null
        }

        description?.let {
            return invalidLogShotAlert(description = it)
        } ?: run {
            return null
        }
    }

    internal fun disableProgressAndShowAlert(alert: Alert) {
        navigation.disableProgress()
        navigation.alert(alert = alert)
    }

    fun onSaveClicked() {
        scope.launch {
            currentPlayer?.let { player ->
                navigation.enableProgress(Progress())
                val state = logShotMutableStateFlow.value

                shotEntryInvalidAlert(
                    shotsMade = state.shotsMade,
                    shotsMissed = state.shotsMissed,
                    shotsAttemptedMillisecondsValue = convertValueToDate(value = state.shotsTakenDateValue)?.time ?: 0
                )?.let { alert ->
                    disableProgressAndShowAlert(alert = alert)
                } ?: run {
                    val pendingShot = PendingShot(
                        player = player,
                        shotLogged = ShotLogged(
                            id = 0,
                            shotName = state.shotName,
                            shotType = currentDeclaredShot?.id ?: 0,
                            shotsAttempted = state.shotsAttempted,
                            shotsMade = state.shotsMade,
                            shotsMissed = state.shotsMissed,
                            shotsMadePercentValue = convertPercentageToDouble(percentage = state.shotsMadePercentValue),
                            shotsMissedPercentValue = convertPercentageToDouble(percentage = state.shotsMissedPercentValue),
                            shotsAttemptedMillisecondsValue = convertValueToDate(value = state.shotsTakenDateValue)?.time
                                ?: 0L,
                            shotsLoggedMillisecondsValue = convertValueToDate(value = state.shotsLoggedDateValue)?.time
                                ?: 0L,
                            isPending = true
                        ),
                        isPendingPlayer = isExistingPlayer
                    )

                    if (viewCurrentExistingShot) {
                        noChangesForShotAlert(pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
                            disableProgressAndShowAlert(alert = alert)
                        } ?: createPendingShot(
                            isACurrentPlayerShot = true,
                            pendingShot = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = shotId))
                        )
                    } else if (viewCurrentPendingShot) {
                        noChangesForShotAlert(pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
                            disableProgressAndShowAlert(alert = alert)
                        } ?: updatePendingShot(pendingShot = pendingShot)
                    } else {
                        createPendingShot(
                            isACurrentPlayerShot = false,
                            pendingShot = pendingShot
                        )
                    }
                }
            } ?: navigation.alert(
                alert = invalidLogShotAlert(
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
                shotsMissedPercentValue = ""
            )
        }

    internal fun noChangesForShotAlert(pendingShotLogged: ShotLogged): Alert? {
        initialShotLogged?.let { currentShot ->
            if (currentShot.isTheSame(pendingShotLogged)) {
                return Alert(
                    title = application.getString(StringsIds.noChangesMade),
                    dismissButton = AlertConfirmAndDismissButton(
                        buttonText = application.getString(StringsIds.gotIt)
                    ),
                    description = application.getString(StringsIds.currentShotHasNoChangesDescription)
                )
            } else {
                return null
            }
        } ?: return null
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

    fun convertPercentageToDouble(percentage: String): Double {
        if (!percentage.contains("%")) {
            return 0.0
        }

        val valueWithoutPercentSign = percentage.replace("%", "")

        val valueWithDecimal = if (!valueWithoutPercentSign.contains(".")) {
            "$valueWithoutPercentSign.0"
        } else {
            valueWithoutPercentSign
        }

        return try {
            valueWithDecimal.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    fun navigateToCreateOrEditPlayer() {
        navigation.disableProgress()
        if (isExistingPlayer) {
            navigation.popToEditPlayer()
        } else {
            navigation.popToCreatePlayer()
        }
    }

    fun convertValueToDate(value: String): Date? {
        return if (value.isEmpty()) {
            null
        } else {
            parseValueToDate(value = value)
        }
    }

    fun onBackClicked() = navigation.pop()

    fun deleteShotAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, logShotMutableStateFlow.value.shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = {}
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no),
                onButtonClicked = {}
            )
        )
    }

    fun onDeleteShotClicked() = navigation.alert(alert = deleteShotAlert())
}
