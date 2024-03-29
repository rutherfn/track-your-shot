package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser

interface ActiveUserRepository {
    suspend fun createActiveUser(activeUser: ActiveUser)
    suspend fun updateActiveUser(activeUser: ActiveUser)
    suspend fun deleteActiveUser()
    suspend fun fetchActiveUser(): ActiveUser?
}
