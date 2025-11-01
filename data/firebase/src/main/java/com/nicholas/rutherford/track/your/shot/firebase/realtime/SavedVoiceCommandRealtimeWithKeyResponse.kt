package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-18
 *
 * Represents a saved voice command information in Firebase Realtime Database along with its unique Firebase key.
 *
 * @property savedVoiceCommandKey The unique Firebase key associated with this saved voice command.
 * @property savedVoiceCommandInfo The actual saved voice command information stored in Firebase.
 */
data class SavedVoiceCommandRealtimeWithKeyResponse(
    val savedVoiceCommandKey: String,
    val savedVoiceCommandInfo: SavedVoiceCommandRealtimeResponse
)
