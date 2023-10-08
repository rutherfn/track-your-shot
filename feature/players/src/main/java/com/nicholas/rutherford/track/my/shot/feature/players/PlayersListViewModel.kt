package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val activeUserRepository: ActiveUserRepository,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo
) : ViewModel() {

    private val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    var isValidPlayer: Boolean = false
    var playerList: List<PlayerInfoRealtimeResponse> = emptyList()

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    init {
        println("get here adada121")
        viewModelScope.launch {
            val activeUser = activeUserRepository.fetchActiveUser()

            activeUser?.firebaseAccountInfoKey?.let { key ->
                deleteFirebaseUserInfo.deletePlayer(key, "-NgBYJxvXcIzW9fVlj5r").collectLatest { result ->
                    if (result) {
                        println("was able to get created")
                    } else {
                        println("was not able to get created")
                    }
                }
            }
        }
    }

    fun validatePlayer(player: Player) {
        if (player.firstName.isNotEmpty() && player.lastName.isNotEmpty()) {
            isValidPlayer = true
        } else {
            isValidPlayer = false
        }
    }
}
