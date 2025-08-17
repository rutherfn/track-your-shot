package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a shot logged in Firebase Realtime Database.
 * Contains detailed statistics for a specific shot, including attempts, makes, misses,
 * percentage values, timing information, and whether the shot is still pending.
 *
 * @property id Unique identifier for the shot.
 * @property shotName Name or title of the shot.
 * @property shotType Numeric representation of the shot type.
 * @property shotsAttempted Total number of shot attempts.
 * @property shotsMade Total number of successful shots made.
 * @property shotsMissed Total number of shots missed.
 * @property shotsMadePercentValue Percentage of shots made.
 * @property shotsMissedPercentValue Percentage of shots missed.
 * @property shotsAttemptedMillisecondsValue Total milliseconds spent attempting the shot.
 * @property shotsLoggedMillisecondsValue Total milliseconds logged for this shot.
 * @property isPending Whether this shot is still pending confirmation or processing.
 */
data class ShotLoggedRealtimeResponse(
    val id: Int = 0,
    val shotName: String = "",
    val shotType: Int = 0,
    val shotsAttempted: Int = 0,
    val shotsMade: Int = 0,
    val shotsMissed: Int = 0,
    val shotsMadePercentValue: Double = 0.0,
    val shotsMissedPercentValue: Double = 0.0,
    val shotsAttemptedMillisecondsValue: Long = 0L,
    val shotsLoggedMillisecondsValue: Long = 0L,
    val isPending: Boolean = false
)
