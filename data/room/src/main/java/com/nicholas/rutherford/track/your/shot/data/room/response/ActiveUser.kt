package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents an active user in the application with account status and identification details.
 *
 * @property id Unique identifier for the user.
 * @property accountHasBeenCreated Indicates whether the user's account has been successfully created.
 * @property email User's email address.
 * @property username User's chosen username.
 * @property firebaseAccountInfoKey Optional Firebase key associated with the user's account info.
 */
data class ActiveUser(
    val id: Int,
    val accountHasBeenCreated: Boolean,
    val email: String,
    val username: String,
    val firebaseAccountInfoKey: String?
)

/**
 * Converts an [ActiveUser] instance to an [ActiveUserEntity] suitable for Room database storage.
 *
 * @return [ActiveUserEntity] instance with the same property values.
 */
fun ActiveUser.toActiveUserEntity(): ActiveUserEntity {
    return ActiveUserEntity(
        id = id,
        accountHasBeenCreated = accountHasBeenCreated,
        email = email,
        username = username,
        firebaseAccountInfoKey = firebaseAccountInfoKey
    )
}
