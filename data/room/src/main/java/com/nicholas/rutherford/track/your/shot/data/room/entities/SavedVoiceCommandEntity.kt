package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

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
@Entity(tableName = "savedVoiceCommands")
data class SavedVoiceCommandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: VoiceCommandTypes
)

fun SavedVoiceCommandEntity.toSavedVoiceCommand(): SavedVoiceCommand {
    return SavedVoiceCommand(
        id = id,
        name = name,
        type = type
    )
}
