package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a position filter in the active player filter.
 * This is a junction table that stores the selected positions for filtering.
 * Multiple positions can be selected for a single filter.
 *
 * @property filterId Foreign key referencing the active filter (always 1).
 * @property position The player position to filter by, stored as a string (e.g., "PG", "SG", "SF", "PF", "C").
 */
@Entity(
    tableName = "playerFilterPositions",
    primaryKeys = ["filterId", "position"],
    foreignKeys = [
        ForeignKey(
            entity = PlayerFilterEntity::class,
            parentColumns = ["id"],
            childColumns = ["filterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlayerFilterPositionEntity(
    @ColumnInfo(name = "filterId")
    val filterId: Int = 1,
    @ColumnInfo(name = "position")
    val position: String
)
