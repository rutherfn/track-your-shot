package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [UserEntity] table.
 */
@Dao
interface UserDao {

    /**
     * Inserts a single user into the database.
     *
     * @param userEntity The user entity to insert.
     */
    @Insert
    suspend fun insert(userEntity: UserEntity)

    /**
     * Inserts multiple users into the database. Replaces existing entries on conflict.
     *
     * @param users The list of user entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    /**
     * Updates an existing user in the database.
     *
     * @param userEntity The user entity to update.
     */
    @Update
    suspend fun update(userEntity: UserEntity)

    /**
     * Deletes a specific user from the database.
     *
     * @param userEntity The user entity to delete.
     */
    @Delete
    suspend fun delete(userEntity: UserEntity)

    /**
     * Deletes all users from the database.
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to fetch.
     * @return The [UserEntity] with the matching email, or null if not found.
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all [UserEntity] objects in the database.
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
}
