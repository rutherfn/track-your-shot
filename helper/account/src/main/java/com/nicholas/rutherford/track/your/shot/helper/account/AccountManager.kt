package com.nicholas.rutherford.track.your.shot.helper.account

import kotlinx.coroutines.flow.Flow

interface AccountManager {
    val hasLoggedInSuccessfulFlow: Flow<Boolean>

    fun logout()
    fun checkIfWeNeedToLogoutOnLaunch()
    fun login(email: String, password: String)
    fun deleteAllPendingShotsAndPlayers()
}
