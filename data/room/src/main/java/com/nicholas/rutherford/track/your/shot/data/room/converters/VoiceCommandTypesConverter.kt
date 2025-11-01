package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Make
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Miss
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.None
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Start
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Stop
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-01
 *
 * A Room TypeConverter for converting between [VoiceCommandTypes] sealed class instances
 * and their corresponding integer representations for database storage.
 */
class VoiceCommandTypesConverter {

    /**
     * Converts an integer value from the database into a corresponding [VoiceCommandTypes] sealed class instances .
     *
     * @param value The integer value representing a [VoiceCommandTypes] sealed class instance.
     * @return The corresponding [VoiceCommandTypes] sealed class instance.
     */
    @TypeConverter
    fun fromValue(value: Int): VoiceCommandTypes {
        return when (value) {
            Constants.VOICE_COMMAND_START_VALUE -> Start
            Constants.VOICE_COMMAND_STOP_VALUE -> Stop
            Constants.VOICE_COMMAND_MAKE_VALUE -> Make
            Constants.VOICE_COMMAND_MISS_VALUE -> Miss
            else -> None
        }
    }

    /**
     * Converts a [VoiceCommandTypes] sealed class instance to its corresponding integer representation
     * for storing in the database.
     *
     * @param voiceCommandType The [VoiceCommandTypes] sealed class instance to convert.
     * @return The integer value corresponding to the voice command type.
     */
    @TypeConverter
    fun toValue(voiceCommandType: VoiceCommandTypes): Int {
        return when (voiceCommandType) {
            is Start -> Constants.VOICE_COMMAND_START_VALUE
            is Stop -> Constants.VOICE_COMMAND_STOP_VALUE
            is Make -> Constants.VOICE_COMMAND_MAKE_VALUE
            is Miss -> Constants.VOICE_COMMAND_MISS_VALUE
            else -> Constants.VOICE_COMMAND_NONE_VALUE
        }
    }
}
