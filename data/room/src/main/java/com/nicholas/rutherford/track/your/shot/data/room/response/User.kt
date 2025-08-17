package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a user in the application.
 *
 * @property id Unique identifier for the user.
 * @property email The user's email address.
 * @property username The user's username.
 */
data class User(
    val id: Int,
    val email: String,
    val username: String
)

/**
 * Converts this [User] object to its corresponding [UserEntity] representation
 * for persistence in the Room database.
 *
 * @return A [UserEntity] instance with the same data.
 */
fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        username = username
    )
}
