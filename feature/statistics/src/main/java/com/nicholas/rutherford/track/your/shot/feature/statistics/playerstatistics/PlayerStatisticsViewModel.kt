package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.feature.statistics.toPlayerStatisticsSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * ViewModel responsible for managing and exposing the state and user interactions
 * for the Player Statistics screen.
 *
 * @param savedStateHandle Provides saved state access to retrieve player id param.
 * @param application Application context used to access string resources.
 * @param scope Coroutine scope used to launch background operations.
 * @param navigation Interface to handle navigation events and alerts.
 * @param playerRepository Repository used to fetch player and shot information from local storage.
 */
class PlayerStatisticsViewModel(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayerStatisticsNavigation,
    private val playerRepository: PlayerRepository
) : BaseViewModel() {

    internal val playerStatisticsMutableStateFlow = MutableStateFlow(value = PlayerStatisticsState())
    val playerStatisticsStateFlow = playerStatisticsMutableStateFlow.asStateFlow()

    private val playerIdParam: Int? = savedStateHandle.get<Int>("playerIdParam")

    init {
        updateStatisticsForPlayer()
    }

    internal fun updateStatisticsForPlayer() {
        playerIdParam?.let { id ->
            scope.launch {
                playerRepository.fetchPlayerById(id = id)?.let { player ->
                    playerStatisticsMutableStateFlow.update { state ->
                        state.copy(playerStatistics = listOf(player.toPlayerStatisticsSummary()))
                    }
                } ?: navigation.pop()
            }
        } ?: navigation.pop()
    }

    fun onToolbarMenuClicked() = navigation.pop()
}
