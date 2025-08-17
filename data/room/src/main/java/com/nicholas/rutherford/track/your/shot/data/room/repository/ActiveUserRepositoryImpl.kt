package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.toActiveUserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for ActiveUser data, providing CRUD operations.
 * Abstracts access to ActiveUserDao and allows use of ActiveUser domain models
 * rather than database entities.
 */
class ActiveUserRepositoryImpl(private val activeUserDao: ActiveUserDao) : ActiveUserRepository {

    /** Inserts a new active user into the database. */
    override suspend fun createActiveUser(activeUser: ActiveUser) =
        activeUserDao.insert(activeUserEntity = activeUser.toActiveUserEntity())

    /** Updates an existing active user in the database. */
    override suspend fun updateActiveUser(activeUser: ActiveUser) =
        activeUserDao.update(activeUserEntity = activeUser.toActiveUserEntity())

    /** Deletes the active user from the database. */
    override suspend fun deleteActiveUser() = activeUserDao.delete()

    /** Fetches the active user from the database, or null if none exists. */
    override suspend fun fetchActiveUser(): ActiveUser? =
        activeUserDao.getActiveUser()?.toActiveUser()
}
