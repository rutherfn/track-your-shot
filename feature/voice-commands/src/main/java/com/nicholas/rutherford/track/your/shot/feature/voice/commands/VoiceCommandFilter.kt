package com.nicholas.rutherford.track.your.shot.feature.voice.commands

enum class VoiceCommandFilter {
    VIEW_ALL,
    START,
    STOP,
    MAKE,
    MISS
}

fun VoiceCommandFilter.toDisplayLabel(): String {
    return when (this) {
        VoiceCommandFilter.VIEW_ALL -> "View All"
        VoiceCommandFilter.START -> "Start"
        VoiceCommandFilter.STOP -> "Stop"
        VoiceCommandFilter.MAKE -> "Make"
        VoiceCommandFilter.MISS -> "Miss"
    }
}