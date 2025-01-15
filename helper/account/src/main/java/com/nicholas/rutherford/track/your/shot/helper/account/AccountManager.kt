package com.nicholas.rutherford.track.your.shot.helper.account

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AccountManager {
    val loggedInPlayerListStateFlow: StateFlow<List<Player>>
    val loggedInDeclaredShotListStateFlow: StateFlow<List<DeclaredShot>>
    val hasLoggedInSuccessfulFlow: Flow<Boolean>
    val hasNoPlayersFlow: Flow<Boolean>

    fun logout()
    fun checkIfWeNeedToLogoutOnLaunch()
    fun login(email: String, password: String)
    fun deleteAllPendingShotsAndPlayers()
    fun updateLoggedInDeclaredShotFlow(declaredShots: List<DeclaredShot>)
}
