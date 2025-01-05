package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity

data class Player(
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val firebaseKey: String,
    val imageUrl: String?,
    val shotsLoggedList: List<ShotLogged>
)

fun List<Player>.buildPlayersWithShots(): List<Player> = this.filter { it.shotsLoggedList.isNotEmpty() }

fun List<Player>.sortedPlayers(): List<Player> = this.sortedWith(compareBy({ it.firstName }, { it.lastName }))

fun List<Player>.getAllShots(): List<ShotLogged> = this.flatMap { it.shotsLoggedList }

fun Player.fullName() = "$firstName $lastName"

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        id = 0,
        firstName = firstName,
        lastName = lastName,
        position = position,
        firebaseKey = firebaseKey,
        imageUrl = imageUrl,
        shotsLoggedList = shotsLoggedList
    )
}

fun Player.toPendingPlayerEntity(): PendingPlayerEntity {
    return PendingPlayerEntity(
        id = 0,
        firstName = firstName,
        lastName = lastName,
        position = position,
        firebaseKey = firebaseKey,
        imageUrl = imageUrl,
        shotsLoggedList = shotsLoggedList
    )
}
