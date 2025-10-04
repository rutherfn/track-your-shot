package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.SavedVoiceCommandEntity

data class SavedVoiceCommand(
    val id: Int,
    val name: String,
    val type: VoiceCommandTypes
)

fun SavedVoiceCommand.toSavedVoiceCommandEntity(): SavedVoiceCommandEntity {
    return SavedVoiceCommandEntity(
        id = id,
        name = name,
        type = type
    )
}
