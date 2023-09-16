package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
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

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()
}