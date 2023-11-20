package com.nicholas.rutherford.track.your.shot.feature.players

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    internal val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    fun updatePlayerListState() {
        scope.launch {
            playerListMutableStateFlow.value = PlayersListState(playerList = playerRepository.fetchAllPlayers())
        }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()
}
