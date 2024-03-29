package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogShotViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    internal val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    private var isExistingPlayer = false
    private var playerId = 0
    private var shotId = 0

    fun updateIsExistingPlayerAndId(
        isExistingPlayerArgument: Boolean,
        playerIdArgument: Int,
        shotIdArgument: Int
    ) {
        this.isExistingPlayer = isExistingPlayerArgument
        this.playerId = playerIdArgument
        this.shotId = shotIdArgument

        scope.launch {
            val declaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = shotId)
            val player = if (isExistingPlayer) {
                playerRepository.fetchPlayerById(id = playerId)
            } else {
                pendingPlayerRepository.fetchPlayerById(id = playerId)
            }

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

    internal fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int {
        return if (shotsMade != 0 || shotsMissed != 0) {
            shotsMade + shotsMissed
        } else {
            0
        }
    }

    internal fun shotsPercentValue(shotsMade: Double, shotsMissed: Double, isShotsMade: Boolean): Double {
        val totalShots = if (isShotsMade) {
            shotsMade + shotsMissed
        } else {
            shotsMissed + shotsMade
        }
        val percentValue = if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
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

    internal fun percentageFormat(shotsMade: Double, shotsMissed: Double, isShotsMade: Boolean): String {
        val percentage = shotsPercentValue(shotsMade = shotsMade, shotsMissed = shotsMissed, isShotsMade = isShotsMade)
        return if (percentage == Constants.SHOT_ZERO_VALUE) {
            application.getString(StringsIds.empty)
        } else {
            val percentageRoundedValue = String.format("%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue.substring(0, percentageRoundedValue.length - 2))
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

    internal fun updateShotsMissedState(shots: Int) {
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
                    updateStateAfterShotsMadeInput(shots = shots.toInt())
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
                    updateShotsMissedState(shots = shots.toInt())
                }
            )
        )
    }

    fun onBackClicked() {
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
        navigation.pop()
    }
}
