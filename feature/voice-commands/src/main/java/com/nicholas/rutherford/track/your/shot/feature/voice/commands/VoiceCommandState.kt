package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandScreen

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-01
 *
 * Represents the different types of states you could be in when on the [CreateEditVoiceCommandScreen]
 * These states include: [RECORDING_NEW], [RECORDING_ERROR], [EDITING_EXISTING] and [CREATING]
 *
 */
enum class VoiceCommandState {
    RECORDING_NEW,
    RECORDING_ERROR,
    EDITING_EXISTING,
    CREATING
}
