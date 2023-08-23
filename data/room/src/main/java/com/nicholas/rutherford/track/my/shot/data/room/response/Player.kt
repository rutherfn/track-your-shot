package com.nicholas.rutherford.track.my.shot.data.room.response

import com.nicholas.rutherford.track.my.shot.data.room.entities.PlayerEntity

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val imageUrl: String?
)

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        position = position,
        imageUrl = imageUrl
    )
}
