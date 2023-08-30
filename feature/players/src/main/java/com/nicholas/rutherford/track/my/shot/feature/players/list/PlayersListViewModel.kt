package com.nicholas.rutherford.track.my.shot.feature.players.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.firebase.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.data.room.repository.activeuser.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.player.PlayerRepository
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val activeUserRepository: ActiveUserRepository,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            fetchActiveUser()
            //  updatePlayerListStateValue()
        }
    }

    private suspend fun fetchActiveUser() {
        val activeUser = activeUserRepository.fetchActiveUser()

        activeUser?.let { user ->
            readFirebaseUserInfo.getAccountInfoKeyByEmail(user.email)
                .collectLatest { test ->
                    test?.let { test2 ->
                        createFirebaseUserInfo
                            .attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                                key = test2,
                                playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                                    firstName = "first",
                                    lastName = "last",
                                    positionValue = 1,
                                    imageUrl = ""
                                )
                            ).collectLatest {
                                it2 ->
                                if (it2) {
                                    println("it works")
                                } else {
                                    println("nope")
                                }
                            }
                    }
                }
        }
    }

    private suspend fun updatePlayerListStateValue() {
        playerListMutableStateFlow.value = playerListStateFlow.value.copy(playerList = playerRepository.fetchAllPlayers())
    }
}
