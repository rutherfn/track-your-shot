package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-16
 *
 * Utility class for creating test instances of [SavedVoiceCommandRealtimeWithKeyResponse].
 * Provides predefined test data for voice command key and voice command information.
 */
object TestSavedVoiceCommandWithKeyRealtimeResponse {

    /**
     * Creates a test [SavedVoiceCommandRealtimeWithKeyResponse] instance with predefined values.
     *
     * @return a [SavedVoiceCommandRealtimeWithKeyResponse] containing test key and voice command info.
     */
    fun create(): SavedVoiceCommandRealtimeWithKeyResponse =
        SavedVoiceCommandRealtimeWithKeyResponse(
            savedVoiceCommandKey = SAVED_VOICE_COMMAND_KEY,
            savedVoiceCommandInfo = TestSavedVoiceCommandRealtimeResponse.create()
        )

    const val SAVED_VOICE_COMMAND_KEY = "key"
}
