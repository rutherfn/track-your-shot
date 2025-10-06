package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

enum class VoiceCommandFilter {
    START,
    STOP,
    MAKE,
    MISS
}

fun VoiceCommandFilter.toDisplayLabel(): String {
    return when (this) {
        VoiceCommandFilter.START -> "Start"
        VoiceCommandFilter.STOP -> "Stop"
        VoiceCommandFilter.MAKE -> "Make"
        VoiceCommandFilter.MISS -> "Miss"
    }
}

    fun VoiceCommandFilter.toType(): VoiceCommandTypes {
        return when (this) {
            VoiceCommandFilter.START -> VoiceCommandTypes.Start
            VoiceCommandFilter.STOP -> VoiceCommandTypes.Stop
            VoiceCommandFilter.MAKE -> VoiceCommandTypes.Make
            VoiceCommandFilter.MISS -> VoiceCommandTypes.Miss
        }
    }