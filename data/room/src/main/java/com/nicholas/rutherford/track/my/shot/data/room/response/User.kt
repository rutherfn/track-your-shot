package com.nicholas.rutherford.track.my.shot.data.room.response

import com.nicholas.rutherford.track.my.shot.data.room.entities.UserEntity

data class User(
    val id: Int,
    val email: String,
    val username: String
)

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        username = username
    )
}
