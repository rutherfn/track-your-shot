package com.nicholas.rutherford.track.your.shot.helper.account

import kotlinx.coroutines.flow.Flow

interface AccountManager {
    val hasLoggedInSuccessfulFlow: Flow<Boolean>

    suspend fun createActiveUser(username: String, email: String)
    fun logout()
    fun checkIfWeNeedToLogoutOnLaunch()
    fun login(email: String, password: String)
    fun deleteAllPendingShotsAndPlayers()
}
