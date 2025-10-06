package com.nicholas.rutherford.track.your.shot.feature.voice.commands

data class VoiceCommandsParams(
    val state: VoiceCommandsState,
    val onToolbarMenuClicked: () -> Unit,
    val onFilterSelected: (filter: VoiceCommandFilter) -> Unit,
    val onCreateCommandTypeClicked: (type: Int?, phrase: String?) -> Unit
)