package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogShotViewModel(
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
        }

        println("$isExistingPlayer$playerId$shotId")

        // update selected player info
        // update declared shot
    }

    fun onDateShotsTakenClicked() {
        val datePickerInfo = DatePickerInfo(
            onDateOkClicked = {
            },
            onDismissClicked = {
            },
            dateValue = "January 1, 2023"
        )
        navigation.datePicker(datePickerInfo = datePickerInfo)
    }

    fun onBackClicked() = navigation.pop()
}
