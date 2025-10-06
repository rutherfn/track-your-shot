package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createvoicecommand

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

data class CreateVoiceCommandState(
    val type: VoiceCommandTypes? = null,
    val recordedPhrase: String? = null
)