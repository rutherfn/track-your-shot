package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a player stored in the local database.
 * This entity stores player information including personal details,
 * position, associated Firebase key, image, and logged shots.
 *
 * @property id Auto-generated unique identifier for the player.
 * @property firstName The player's first name.
 * @property lastName The player's last name.
 * @property position The player's position on the team as a [PlayerPositions] value.
 * @property firebaseKey The Firebase key associated with this player.
 * @property imageUrl Optional URL to the player's profile image.
 * @property shotsLoggedList List of [ShotLogged] objects representing shots logged for this player.
 */
@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "firstName")
    val firstName: String,

    @ColumnInfo(name = "lastName")
    val lastName: String,

    @ColumnInfo(name = "position")
    val position: PlayerPositions,

    @ColumnInfo(name = "firebaseKey")
    val firebaseKey: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "shotsLogged")
    val shotsLoggedList: List<ShotLogged>
)

/**
 * Converts a [PlayerEntity] to its corresponding domain model [Player].
 *
 * @return A [Player] instance with values mapped from this entity.
 */
fun PlayerEntity.toPlayer(): Player {
    return Player(
        firstName = firstName,
        lastName = lastName,
        position = position,
        firebaseKey = firebaseKey,
        imageUrl = imageUrl,
        shotsLoggedList = shotsLoggedList
    )
}
