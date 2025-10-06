package com.nicholas.rutherford.track.your.shot.data.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-01
 *
 * Represents the voice command types you define by recording your voice through commands
 * Uses a sealed class to define all possible voice commands with there types including a default [None].
 *
 * @property value Integer value representing this voice command.
 */
sealed class VoiceCommandTypes(val value: Int) {

    /** Start Voice Command */
    data object Start : VoiceCommandTypes(value = Constants.VOICE_COMMAND_START_VALUE)

    /** Stop Voice Command */
    data object Stop : VoiceCommandTypes(value = Constants.VOICE_COMMAND_STOP_VALUE)

    /** Make Voice Command */
    data object Make : VoiceCommandTypes(value = Constants.VOICE_COMMAND_MAKE_VALUE)

    /** Miss Voice Command */
    data object Miss : VoiceCommandTypes(value = Constants.VOICE_COMMAND_MISS_VALUE)

    /** None Voice Command */
    data object None : VoiceCommandTypes(value = Constants.VOICE_COMMAND_NONE_VALUE)

    companion object {
        /**
         * Converts an integer value to its corresponding [VoiceCommandTypes] instance.
         * Defaults to [None] if the value does not match any known voice commands.
         *
         * @param value Integer representation of a voice command
         * @return Corresponding [VoiceCommandTypes] instance.
         */
        fun fromValue(value: Int): VoiceCommandTypes {
            return when (value) {
                Constants.VOICE_COMMAND_START_VALUE -> Start
                Constants.VOICE_COMMAND_STOP_VALUE -> Stop
                Constants.VOICE_COMMAND_MAKE_VALUE -> Make
                Constants.VOICE_COMMAND_MISS_VALUE -> Miss
                else -> None
            }
        }

        fun VoiceCommandTypes.toDisplayLabel(): String {
            return when (this) {
                VoiceCommandTypes.Start -> "Start"
                VoiceCommandTypes.Stop -> "Stop"
                VoiceCommandTypes.Make -> "Make"
                VoiceCommandTypes.Miss -> "Miss"
                VoiceCommandTypes.None -> "None"
            }
        }

        /**
         * Converts a [VoiceCommandTypes] instance to a localized string representing the voice command.
         *
         * @param application Application context for accessing string resources.
         * @return Localized string for the voiceCommand.
         */
        fun VoiceCommandTypes.toVoiceCommandValue(application: Application): String {
            return when (this) {
                Start -> application.getString(StringsIds.start)
                Stop -> application.getString(StringsIds.stop)
                Make -> application.getString(StringsIds.make)
                Miss -> application.getString(StringsIds.miss)
                else -> application.getString(StringsIds.none)
            }
        }

        /**
         * Converts a localized string to its corresponding [VoiceCommandTypes] instance.
         * Defaults to [None] if the string does not match any known voice command types.
         *
         * @param application Application context for accessing string resources.
         * @return Corresponding [VoiceCommandTypes] instance.
         */
        fun String.toPlayVoiceCommandsTypes(application: Application): VoiceCommandTypes {
            return when (this) {
                application.getString(StringsIds.start) -> Start
                application.getString(StringsIds.stop) -> Stop
                application.getString(StringsIds.make) -> Make
                application.getString(StringsIds.miss) -> Miss
                else -> None
            }
        }
    }
}
