package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a shot that should be ignored in statistics or calculations.
 *
 * @property id Auto-generated unique identifier for the ignored shot entry.
 * @property shotId The ID of the shot that should be ignored.
 */
@Entity(tableName = "shotsIgnoring")
data class ShotIgnoringEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "shotId")
    val shotId: Int
)

/**
 * Converts a [ShotIgnoringEntity] to its corresponding domain model [ShotIgnoring].
 *
 * @return A [ShotIgnoring] instance with values mapped from this entity.
 */
fun ShotIgnoringEntity.toShotIgnoring(): ShotIgnoring {
    return ShotIgnoring(id = id, shotId = shotId)
}
