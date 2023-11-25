package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatePlayerViewModel(
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val scope: CoroutineScope,
    private val navigation: CreatePlayerNavigation,
    private val network: Network
) : ViewModel() {

    internal val createPlayerMutableStateFlow = MutableStateFlow(value = CreatePlayerState())
    val createPlayerStateFlow = createPlayerMutableStateFlow.asStateFlow()

    fun onToolbarMenuClicked() {

    }

    fun onImageUploadClicked() {

    }

    fun onCreatePlayerClicked() {

    }
}