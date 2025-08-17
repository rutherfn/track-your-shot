package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a basketball player with personal details, position, and logged shots.
 *
 * @property firstName Player's first name.
 * @property lastName Player's last name.
 * @property position Player's court position.
 * @property firebaseKey Firebase identifier for the player.
 * @property imageUrl Optional URL for the player's profile image.
 * @property shotsLoggedList List of [ShotLogged] representing the player's shot history.
 */
data class Player(
    val firstName: String,
    val lastName: String,
    val position: PlayerPositions,
    val firebaseKey: String,
    val imageUrl: String?,
    val shotsLoggedList: List<ShotLogged>
)

/**
 * Filters the list of players to include only those with at least one logged shot.
 *
 * @return List of players with non-empty shot logs.
 */
fun List<Player>.buildPlayersWithShots(): List<Player> = this.filter { it.shotsLoggedList.isNotEmpty() }

/**
 * Sorts players alphabetically by first name, then last name.
 *
 * @return Sorted list of players.
 */
fun List<Player>.sortedPlayers(): List<Player> = this.sortedWith(compareBy({ it.firstName }, { it.lastName }))

/**
 * Collects all [ShotLogged] objects from a list of players into a single flat list.
 *
 * @return List of all shots from all players.
 */
fun List<Player>.getAllShots(): List<ShotLogged> = this.flatMap { it.shotsLoggedList }

/**
 * Returns the full name of the player by combining first and last names.
 *
 * @return Full name string.
 */
fun Player.fullName() = "$firstName $lastName"

/**
 * Converts a [Player] instance to a [PlayerEntity] for Room database insertion.
 *
 * @return Corresponding [PlayerEntity] with auto-generated id (set to 0).
 */
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

/**
 * Converts a [Player] instance to a [PendingPlayerEntity] for Room database insertion.
 *
 * @return Corresponding [PendingPlayerEntity] with auto-generated id (set to 0).
 */
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
