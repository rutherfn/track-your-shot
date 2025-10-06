package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand

data class VoiceCommandsState(
    val startCommands: List<SavedVoiceCommand> = emptyList(),
    val stopCommands: List<SavedVoiceCommand> = emptyList(),
    val makeCommands: List<SavedVoiceCommand> = emptyList(),
    val missCommands: List<SavedVoiceCommand> = emptyList(),
    val noneCommands: List<SavedVoiceCommand> = emptyList(),
    val selectedFilter: VoiceCommandFilter = VoiceCommandFilter.START,
    val filteredCommands: List<SavedVoiceCommand> = emptyList()
)