package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.SavedVoiceCommandEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

object TestSavedVoiceCommandEntity {

    fun build(): SavedVoiceCommandEntity {
        return SavedVoiceCommandEntity(
            id = SAVED_VOICE_COMMAND_ID,
            name = SAVED_VOICE_COMMAND_NAME,
            type = SAVED_VOICE_COMMAND_TYPE
        )
    }

    const val SAVED_VOICE_COMMAND_ID = 0
    const val SAVED_VOICE_COMMAND_NAME = "name"
    val SAVED_VOICE_COMMAND_TYPE = VoiceCommandTypes.Start
}