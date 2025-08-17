package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for ActiveUser data.
 * Provides an abstraction layer over the underlying ActiveUserDao,
 * allowing use of ActiveUser domain models instead of database entities.
 */
interface ActiveUserRepository {

    /** Inserts a new active user into the database. */
    suspend fun createActiveUser(activeUser: ActiveUser)

    /** Updates an existing active user in the database. */
    suspend fun updateActiveUser(activeUser: ActiveUser)

    /** Deletes the active user from the database. */
    suspend fun deleteActiveUser()

    /** Fetches the active user from the database, or null if none exists. */
    suspend fun fetchActiveUser(): ActiveUser?
}
