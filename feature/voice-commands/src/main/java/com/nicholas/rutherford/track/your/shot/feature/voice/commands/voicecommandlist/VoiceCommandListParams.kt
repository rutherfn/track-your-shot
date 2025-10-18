package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter

data class VoiceCommandListParams(
    val state: VoiceCommandListState,
    val onToolbarMenuClicked: () -> Unit,
    val onFilterSelected: (filter: VoiceCommandFilter) -> Unit,
    val onCreateEditCommandTypeClicked: (type: Int?, phrase: String?) -> Unit
)
