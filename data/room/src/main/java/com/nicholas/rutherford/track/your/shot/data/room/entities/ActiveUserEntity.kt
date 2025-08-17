package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing the active user in the local database.
 *
 * @property id The unique identifier for the active user.
 * @property accountHasBeenCreated Indicates whether the user account has been created.
 * @property email The email address of the active user.
 * @property username The username of the active user.
 * @property firebaseAccountInfoKey The optional Firebase account info key for the user.
 */
@Entity(tableName = "activeUsers")
data class ActiveUserEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "accountHasBeenCreated")
    val accountHasBeenCreated: Boolean,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "firebaseAccountInfoKey")
    val firebaseAccountInfoKey: String?
)

/**
 * Converts an [ActiveUserEntity] to its corresponding domain model [ActiveUser].
 *
 * @return An [ActiveUser] instance with values mapped from this entity.
 */
fun ActiveUserEntity.toActiveUser(): ActiveUser {
    return ActiveUser(
        id = id,
        accountHasBeenCreated = accountHasBeenCreated,
        email = email,
        username = username,
        firebaseAccountInfoKey = firebaseAccountInfoKey
    )
}
