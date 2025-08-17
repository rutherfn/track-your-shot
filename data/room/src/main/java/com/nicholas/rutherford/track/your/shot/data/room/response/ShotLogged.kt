package com.nicholas.rutherford.track.your.shot.data.room.response

import androidx.room.ColumnInfo

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a single shot logged by a player.
 *
 * @property id Unique identifier for the shot.
 * @property shotName Name of the shot.
 * @property shotType Type/category of the shot.
 * @property shotsAttempted Number of times the shot was attempted.
 * @property shotsMade Number of times the shot was successfully made.
 * @property shotsMissed Number of times the shot was missed.
 * @property shotsMadePercentValue Percentage of successful shots.
 * @property shotsMissedPercentValue Percentage of missed shots.
 * @property shotsAttemptedMillisecondsValue Time spent attempting the shot in milliseconds.
 * @property shotsLoggedMillisecondsValue Time logged for the shot in milliseconds.
 * @property isPending Whether this shot is pending (not finalized).
 */
data class ShotLogged(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "shotName")
    val shotName: String,
    @ColumnInfo(name = "shotType")
    val shotType: Int,
    @ColumnInfo(name = "shotsAttempted")
    val shotsAttempted: Int,
    @ColumnInfo(name = "shotsMade")
    val shotsMade: Int,
    @ColumnInfo(name = "shotsMissed")
    val shotsMissed: Int,
    @ColumnInfo(name = "shotsMadePercentValue")
    val shotsMadePercentValue: Double,
    @ColumnInfo(name = "shotsMissedPercentValue")
    val shotsMissedPercentValue: Double,
    @ColumnInfo(name = "shotsAttemptedMillisecondsValue")
    val shotsAttemptedMillisecondsValue: Long,
    @ColumnInfo(name = "shotsLoggedMillisecondsValue")
    val shotsLoggedMillisecondsValue: Long,
    @ColumnInfo(name = "isPending")
    val isPending: Boolean
)

/**
 * Compares this [ShotLogged] instance with another to check if all key shot details match.
 *
 * @param shotCompared The [ShotLogged] instance to compare against.
 * @return `true` if all properties except `id` and `isPending` match, `false` otherwise.
 */
fun ShotLogged.isTheSame(shotCompared: ShotLogged): Boolean {
    return this.shotName == shotCompared.shotName &&
        this.shotType == shotCompared.shotType &&
        this.shotsAttempted == shotCompared.shotsAttempted &&
        this.shotsMade == shotCompared.shotsMade &&
        this.shotsMissed == shotCompared.shotsMissed &&
        this.shotsMadePercentValue == shotCompared.shotsMadePercentValue &&
        this.shotsMissedPercentValue == shotCompared.shotsMissedPercentValue &&
        this.shotsAttemptedMillisecondsValue == shotCompared.shotsAttemptedMillisecondsValue &&
        this.shotsLoggedMillisecondsValue == shotCompared.shotsLoggedMillisecondsValue
}
