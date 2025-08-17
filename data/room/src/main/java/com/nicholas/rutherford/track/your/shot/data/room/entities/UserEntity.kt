package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.User

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a user in the local database.
 *
 * @property id Unique identifier for the user.
 * @property email The user's email address.
 * @property username The user's chosen username.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String
)

/**
 * Converts a [UserEntity] to its corresponding domain model [User].
 *
 * @return A [User] instance with values mapped from this entity.
 */
fun UserEntity.toUser(): User {
    return User(
        id = id,
        email = email,
        username = username
    )
}
