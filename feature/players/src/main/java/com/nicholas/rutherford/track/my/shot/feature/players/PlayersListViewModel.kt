package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.repository.player.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch { updatePlayerListStateValue() }
    }

    private suspend fun updatePlayerListStateValue() {
        playerListMutableStateFlow.value = playerListStateFlow.value.copy(playerList = playerRepository.fetchAllPlayers())
    }
}
