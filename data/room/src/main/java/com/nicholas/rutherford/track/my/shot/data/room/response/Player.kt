package com.nicholas.rutherford.track.my.shot.data.room.response

data class Player(
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val imageUrl: String?
)
