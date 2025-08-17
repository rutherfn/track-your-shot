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
 * Room entity representing a pending player stored in the local database.
 * This entity is used for players whose data is not yet finalized or synced.
 *
 * @property id Auto-generated unique identifier for the pending player.
 * @property firstName The first name of the player.
 * @property lastName The last name of the player.
 * @property position The player's position on the team as a [PlayerPositions] value.
 * @property firebaseKey The Firebase key associated with this player.
 * @property imageUrl Optional URL to the player's profile image.
 * @property shotsLoggedList List of [ShotLogged] objects representing shots logged for this player.
 */
@Entity(tableName = "pendingPlayers")
data class PendingPlayerEntity(
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
 * Converts a [PendingPlayerEntity] to its corresponding domain model [Player].
 *
 * @return A [Player] instance with values mapped from this pending entity.
 */
fun PendingPlayerEntity.toPlayer(): Player {
    return Player(
        firstName = firstName,
        lastName = lastName,
        position = position,
        firebaseKey = firebaseKey,
        imageUrl = imageUrl,
        shotsLoggedList = shotsLoggedList
    )
}
