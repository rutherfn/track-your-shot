package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListState

data class VoiceCommandListParams(
    val state: VoiceCommandListState,
    val onToolbarMenuClicked: () -> Unit,
    val onFilterSelected: (filter: VoiceCommandFilter) -> Unit,
    val onCreateCommandTypeClicked: (type: Int?, phrase: String?) -> Unit
)