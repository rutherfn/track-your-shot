package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity

data class Player(
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val imageUrl: String?
)

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        id = 1,
        firstName = firstName,
        lastName = lastName,
        position = position,
        imageUrl = imageUrl
    )
}
