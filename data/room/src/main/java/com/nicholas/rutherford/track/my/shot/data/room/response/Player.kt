package com.nicholas.rutherford.track.my.shot.data.room.response

data class Player(
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val imageUrl: String?
)

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        id = 0,
        firstName = firstName,
        lastName = lastName,
        position = position,
        imageUrl = imageUrl
    )
}