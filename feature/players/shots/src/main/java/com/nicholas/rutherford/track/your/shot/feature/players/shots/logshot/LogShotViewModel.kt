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
    private val playerPendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    internal val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    internal var isExistingPlayer: Boolean = false
    internal var playerId: Int = 0
    internal var shotId: Int = 0

    fun updateIsExistingPlayerAndPlayerId(
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
                playerPendingPlayerRepository.fetchPlayerById(id = playerId)
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

    internal fun shotsMadePercentValue(shotsMade: Double, shotsMissed: Double): String {
        val totalShots = shotsMade + shotsMissed
        val percentValue = if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
            val percentage = shotsMade / totalShots * 100
            val percentageRoundedValue = String.format("%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue.substring(0, percentageRoundedValue.length - 2))
            } else {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue)
            }
        } else {
            application.getString(StringsIds.empty)
        }

        return percentValue
    }

    internal fun shotsMissedPercentValue(shotsMade: Double, shotsMissed: Double): String {
        println("shots made $shotsMade")
        println("shots missed $shotsMissed")
        val totalShots = shotsMissed + shotsMade
        val percentValue = if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
            val percentage = shotsMissed / totalShots * 100
            val percentageRoundedValue = String.format("%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue.substring(0, percentageRoundedValue.length - 2))
            } else {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue)
            }
        } else {
            application.getString(StringsIds.empty)
        }

        return percentValue
    }

//    internal fun shosMadePercentValue(shotsMade: Double, shotsMissed: Double): String {
//        if (shotsMade != 0 && shotsMissed != 0 )
//    }

    internal fun updateStateAfterShotsMadeInput(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMade = shots,
                shotsAttempted = shotsAttempted(shotsMade = shots, shotsMissed = state.shotsMissed),
                shotsMadePercentValue = shotsMadePercentValue(shotsMade = shots.toDouble(), shotsMissed = state.shotsMissed.toDouble()),
                shotsMissedPercentValue = shotsMissedPercentValue(shotsMade = shots.toDouble(), shotsMissed = state.shotsMissed.toDouble())
            )
        }
    }

    internal fun updateShotsMissedState(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMissed = shots,
                shotsAttempted = shotsAttempted(shotsMade = state.shotsMade, shotsMissed = shots),
                shotsMadePercentValue = shotsMadePercentValue(shotsMade = state.shotsMade.toDouble(), shotsMissed = shots.toDouble()),
                shotsMissedPercentValue = shotsMissedPercentValue(shotsMade = state.shotsMade.toDouble(), shotsMissed = shots.toDouble())
            )
        }
    }

    fun onShotsMadeClicked() {
        navigation.inputInfo(
            inputInfo = InputInfo(
                titleResId = StringsIds.enterShotsMade,
                confirmButtonResId = StringsIds.ok,
                dismissButtonResId = StringsIds.cancel,
                placeholderResId = StringsIds.shotsMade,
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
                onConfirmButtonClicked = { shots ->
                    updateShotsMissedState(shots = shots.toInt())
                }
            )
        )
    }

    fun onBackClicked() = navigation.pop()
}
