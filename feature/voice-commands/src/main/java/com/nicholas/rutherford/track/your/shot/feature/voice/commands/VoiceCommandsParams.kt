package com.nicholas.rutherford.track.your.shot.feature.voice.commands

data class VoiceCommandsParams(
    val state: VoiceCommandsState,
    val onFilterSelected: (filter: VoiceCommandFilter) -> Unit
)