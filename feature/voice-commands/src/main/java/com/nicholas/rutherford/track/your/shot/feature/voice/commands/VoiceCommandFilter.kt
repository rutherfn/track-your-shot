package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-01
 *
 * Represents the different types of voice commands that can be saved from the user
 */
enum class VoiceCommandFilter {
    START,
    STOP,
    MAKE,
    MISS
}

/**
 * Extension function to convert a [VoiceCommandFilter] to a [String] that can be displayed to the user
 *
 * @return [String]
 */
fun VoiceCommandFilter.toDisplayLabel(): String {
    return when (this) {
        VoiceCommandFilter.START -> "Start"
        VoiceCommandFilter.STOP -> "Stop"
        VoiceCommandFilter.MAKE -> "Make"
        VoiceCommandFilter.MISS -> "Miss"
    }
}

/**
 * Extension function to convert a [VoiceCommandFilter] to a [VoiceCommandTypes] that can be used by the user.
 *
 * @return [VoiceCommandTypes]
 */
fun VoiceCommandFilter.toType(): VoiceCommandTypes {
    return when (this) {
        VoiceCommandFilter.START -> VoiceCommandTypes.Start
        VoiceCommandFilter.STOP -> VoiceCommandTypes.Stop
        VoiceCommandFilter.MAKE -> VoiceCommandTypes.Make
        VoiceCommandFilter.MISS -> VoiceCommandTypes.Miss
    }
}
