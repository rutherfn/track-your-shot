package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandState

data class CreateEditVoiceCommandState(
    val type: VoiceCommandTypes = VoiceCommandTypes.None,
    val recordedPhrase: String? = null,
    val isRecording: Boolean = false,
    val voiceCommandState: VoiceCommandState = VoiceCommandState.CREATING
)
