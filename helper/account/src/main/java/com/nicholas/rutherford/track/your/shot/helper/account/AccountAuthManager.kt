package com.nicholas.rutherford.track.your.shot.helper.account

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import kotlinx.coroutines.flow.StateFlow

interface AccountAuthManager {
    val loggedInPlayerListStateFlow: StateFlow<List<Player>>

    fun logout()
    fun login(email: String, password: String)
}
