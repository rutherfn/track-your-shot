package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.SavedVoiceCommandEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class that represents a SavedVoiceCommand
 * This data class stores saved voice command information
 *
 * @property id Auto-generated unique identifier for the player.
 * @property name [String] Saved value for the user command
 * @property firebaseKey Firebase identifier for this saved voice command.
 * @property type [VoiceCommandTypes] type of command that the saved command is of
 */
data class SavedVoiceCommand(
    val id: Int,
    val name: String,
    val firebaseKey: String,
    val type: VoiceCommandTypes
)

/**
 * Will return back a filter list depending on the [type] passed in
 */
fun List<SavedVoiceCommand>.filterBy(type: VoiceCommandTypes) =
    this.filter { command -> command.type == type }

/**
 * Responsible for converting a [SavedVoiceCommand] to a [SavedVoiceCommandEntity]
 */
fun SavedVoiceCommand.toSavedVoiceCommandEntity(): SavedVoiceCommandEntity {
    return SavedVoiceCommandEntity(
        id = id,
        name = name,
        firebaseKey = firebaseKey,
        type = type
    )
}
