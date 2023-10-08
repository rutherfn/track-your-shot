package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val activeUserRepository: ActiveUserRepository,
    private val readFirebaseUserInfoImpl: ReadFirebaseUserInfo,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
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
                readFirebaseUserInfoImpl.getPlayerInfoList(key).collectLatest { result ->
                    result.forEach {
                        println("here is the raw response $it")
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
