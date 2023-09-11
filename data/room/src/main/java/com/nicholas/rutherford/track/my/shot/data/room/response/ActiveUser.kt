package com.nicholas.rutherford.track.my.shot.data.room.response

import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity

data class ActiveUser(
    val id: Int,
    val accountHasBeenCreated: Boolean,
    val email: String,
    val username: String,
    val firebaseAccountInfoKey: String
)

fun ActiveUser.toActiveUserEntity(): ActiveUserEntity {
    return ActiveUserEntity(
        id = id,
        accountHasBeenCreated = accountHasBeenCreated,
        email = email,
        username = username,
        firebaseAccountInfoKey = firebaseAccountInfoKey
    )
}
