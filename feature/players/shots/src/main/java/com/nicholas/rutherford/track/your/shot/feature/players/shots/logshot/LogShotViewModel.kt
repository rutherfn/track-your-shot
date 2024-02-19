package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LogShotViewModel(
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerPendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository
    ) : ViewModel() {

    internal val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    internal var isExistingPlayer: Boolean? = null
    internal var playerId: Int? = null
    internal var shotId: Int? = null

    fun updateIsExistingPlayerAndPlayerId(
        isExistingPlayerArgument: Boolean?,
        playerIdArgument: Int?,
        shotIdArgument: Int?
    ) {
        this.isExistingPlayer = isExistingPlayerArgument
        this.playerId = playerIdArgument
        this.shotId = shotId

        // update selected player info
        // update declared shot
    }

    fun onBackClicked() = navigation.pop()

}