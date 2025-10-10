package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

data class CreateEditVoiceCommandState(
    val type: VoiceCommandTypes? = null,
    val recordedPhrase: String? = null
)
