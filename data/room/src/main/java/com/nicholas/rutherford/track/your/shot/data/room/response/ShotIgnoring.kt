package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a shot that is marked to be ignored in player statistics.
 *
 * @property id Unique identifier for this ignoring entry.
 * @property shotId The ID of the shot being ignored.
 */
data class ShotIgnoring(
    val id: Int,
    val shotId: Int
)

/**
 * Converts this [ShotIgnoring] instance to its corresponding [ShotIgnoringEntity] for Room database persistence.
 *
 * @return [ShotIgnoringEntity] representing the same ignoring shot data.
 */
fun ShotIgnoring.toShotIgnoringEntity(): ShotIgnoringEntity {
    return ShotIgnoringEntity(id = id, shotId = shotId)
}
