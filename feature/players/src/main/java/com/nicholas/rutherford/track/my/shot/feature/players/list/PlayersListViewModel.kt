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
import kotlinx.coroutines.flow.combine
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
        }
    }

    private suspend fun updateFromFirebaseActivePlayers() {
        // call this function if the players room db is empty
        // then were gotta try to get the instance from firebase
        //
        val activeUser = activeUserRepository.fetchActiveUser() ?: return

        val email = activeUser.email
        val accountInfoKeyFlow = readFirebaseUserInfo.getAccountInfoKeyFlowByEmail(email)

        accountInfoKeyFlow.collectLatest { accountKey ->
            accountKey?.let { key ->
                readFirebaseUserInfo.getPlayerInfoList(accountKey)
                    .collectLatest {
                        // do stuff here
                        // if the value were collecting come back as true
                        // we should still update the rooms read data but update it with a empty list
                        // the empty list would then still be treated as something
                    }
            }
        }
    }

    private suspend fun updatePlayerListStateValue() {
        playerListMutableStateFlow.value = playerListStateFlow.value.copy(playerList = playerRepository.fetchAllPlayers())
    }
}
