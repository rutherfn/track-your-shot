package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toSavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand

object TestSavedVoiceCommand {
    fun build(): SavedVoiceCommand = TestSavedVoiceCommandEntity.build().toSavedVoiceCommand()
}