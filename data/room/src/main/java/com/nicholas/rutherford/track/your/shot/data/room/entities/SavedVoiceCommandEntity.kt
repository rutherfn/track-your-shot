package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a saved voice command.
 * This entity stores saved voice command information
 *
 * @property id Auto-generated unique identifier for the player.
 * @property name [String] Saved value for the user command
 * @property firebaseKey [String] Firebase identifier for this saved voice command.
 * @property type [VoiceCommandTypes] type of command that the saved command is of
 */
@Entity(tableName = "savedVoiceCommands")
data class SavedVoiceCommandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "firebaseKey")
    val firebaseKey: String,
    @ColumnInfo(name = "type")
    val type: VoiceCommandTypes
)

/**
 * Responsible for converting a [SavedVoiceCommandEntity] to a [SavedVoiceCommand]
 */
fun SavedVoiceCommandEntity.toSavedVoiceCommand(): SavedVoiceCommand {
    return SavedVoiceCommand(
        id = id,
        name = name,
        firebaseKey = firebaseKey,
        type = type
    )
}
