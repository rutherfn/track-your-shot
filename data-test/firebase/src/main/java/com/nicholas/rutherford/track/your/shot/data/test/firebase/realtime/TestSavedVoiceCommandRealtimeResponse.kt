package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeResponse

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-16
 *
 * Utility class for creating test instances of [SavedVoiceCommandRealtimeResponse].
 * Provides predefined test data for voice command name and type value.
 */
object TestSavedVoiceCommandRealtimeResponse {

    /**
     * Creates a test [SavedVoiceCommandRealtimeResponse] instance with predefined values.
     *
     * @return a [SavedVoiceCommandRealtimeResponse] containing test name and type value.
     */
    fun create(): SavedVoiceCommandRealtimeResponse =
        SavedVoiceCommandRealtimeResponse(
            name = NAME,
            typeValue = TYPE_VALUE
        )

    const val NAME = "name"

    const val TYPE_VALUE = 5
}
