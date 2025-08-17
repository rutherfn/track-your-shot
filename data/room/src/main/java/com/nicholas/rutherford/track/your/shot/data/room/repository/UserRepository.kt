package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.User

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for User data.
 * This provides an abstraction layer over the underlying UserDao,
 * allowing use of User domain models rather than database entities.
 */
interface UserRepository {

    /** Inserts a single User into the database. */
    suspend fun createUser(user: User)

    /** Inserts a list of Users into the database. */
    suspend fun createUsers(userList: List<User>)

    /** Updates an existing User in the database. */
    suspend fun updateUser(user: User)

    /** Deletes a specific User from the database. */
    suspend fun deleteUser(user: User)

    /** Deletes all Users from the database. */
    suspend fun deleteAllUsers()

    /** Fetches a User by their email, or returns null if not found. */
    suspend fun fetchUserByEmail(email: String): User?

    /** Fetches all Users from the database. */
    suspend fun fetchAllUsers(): List<User>
}
