package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter

data class VoiceCommandListState(
    val startCommands: List<SavedVoiceCommand> = emptyList(),
    val stopCommands: List<SavedVoiceCommand> = emptyList(),
    val makeCommands: List<SavedVoiceCommand> = emptyList(),
    val missCommands: List<SavedVoiceCommand> = emptyList(),
    val noneCommands: List<SavedVoiceCommand> = emptyList(),
    val selectedFilter: VoiceCommandFilter = VoiceCommandFilter.START,
    val filteredCommands: List<SavedVoiceCommand> = emptyList()
) {
    /**
     * Returns true if the start commands category has exactly 1 item
     */
    val hasSingleStartCommand: Boolean
        get() = startCommands.size == 1

    /**
     * Returns true if the stop commands category has exactly 1 item
     */
    val hasSingleStopCommand: Boolean
        get() = stopCommands.size == 1

    /**
     * Returns true if the make commands category has exactly 1 item
     */
    val hasSingleMakeCommand: Boolean
        get() = makeCommands.size == 1

    /**
     * Returns true if the miss commands category has exactly 1 item
     */
    val hasSingleMissCommand: Boolean
        get() = missCommands.size == 1

    /**
     * Returns true if the currently selected filter category has exactly 1 item
     */
    val hasSingleCommandForSelectedFilter: Boolean
        get() = when (selectedFilter) {
            VoiceCommandFilter.START -> hasSingleStartCommand
            VoiceCommandFilter.STOP -> hasSingleStopCommand
            VoiceCommandFilter.MAKE -> hasSingleMakeCommand
            VoiceCommandFilter.MISS -> hasSingleMissCommand
        }
}
