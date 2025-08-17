package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing a Declared Shot retrieved from
 * Firebase Realtime Database. Provides the essential information
 * about a shot for in-app use or display.
 *
 * @property id Unique identifier for the declared shot.
 * @property shotCategory The category of the shot (e.g., inside, mid-range, long-range).
 * @property title The title or name of the shot.
 * @property description A description explaining how the shot is performed.
 */
data class DeclaredShotRealtimeResponse(
    val id: Int = 0,
    val shotCategory: String = "",
    val title: String = "",
    val description: String = ""
)
