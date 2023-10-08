package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayersListViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo
) : ViewModel() {

    private val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    var isValidPlayer: Boolean = false
    var playerList: List<PlayerInfoRealtimeResponse> = emptyList()

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    fun validatePlayer(player: Player) {
        if (player.firstName.isNotEmpty() && player.lastName.isNotEmpty()) {
            isValidPlayer = true
        } else {
            isValidPlayer = false
        }
    }
}
