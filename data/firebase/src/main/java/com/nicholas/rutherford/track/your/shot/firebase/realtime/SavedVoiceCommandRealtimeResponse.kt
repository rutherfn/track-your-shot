package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-16
 *
 * Represents a saved voice command information stored in Firebase Realtime Database.
 *
 * @property name the exact name value of the command
 * @property typeValue the integer value of the command type
 */
data class SavedVoiceCommandRealtimeResponse(
    val name: String = "",
    val typeValue: Int = 4 // 4 represents the None default voice command
)
