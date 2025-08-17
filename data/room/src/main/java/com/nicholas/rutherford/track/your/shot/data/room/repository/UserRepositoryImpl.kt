package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toUser
import com.nicholas.rutherford.track.your.shot.data.room.response.User
import com.nicholas.rutherford.track.your.shot.data.room.response.toUserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [UserRepository] that provides data access operations
 * for [User] entities via [UserDao]. Handles creating, updating, deleting,
 * and fetching users from the Room database.
 *
 * @property userDao The DAO responsible for user database operations.
 */
class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    /**
     * Inserts a single [User] into the database.
     *
     * @param user The user to insert.
     */
    override suspend fun createUser(user: User) = userDao.insert(userEntity = user.toUserEntity())

    /**
     * Inserts multiple [User]s into the database.
     *
     * @param userList List of users to insert.
     */
    override suspend fun createUsers(userList: List<User>) = userDao.insertUsers(users = userList.map { it.toUserEntity() })

    /**
     * Updates an existing [User] in the database.
     *
     * @param user The user to update.
     */
    override suspend fun updateUser(user: User) = userDao.update(userEntity = user.toUserEntity())

    /**
     * Deletes all users from the database.
     */
    override suspend fun deleteAllUsers() = userDao.deleteAllUsers()

    /**
     * Deletes a specific [User] from the database.
     *
     * @param user The user to delete.
     */
    override suspend fun deleteUser(user: User) = userDao.delete(userEntity = user.toUserEntity())

    /**
     * Fetches a user by their email.
     *
     * @param email The email to search for.
     * @return The matching [User] or null if not found.
     */
    override suspend fun fetchUserByEmail(email: String): User? =
        userDao.getUserByEmail(email = email)?.toUser()

    /**
     * Fetches all users from the database.
     *
     * @return List of all [User]s.
     */
    override suspend fun fetchAllUsers(): List<User> {
        return userDao.getAllUsers().map { it.toUser() }
    }
}
