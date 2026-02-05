package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing the active player filter stored in the local database.
 * Only one active filter exists at a time (id = 1).
 * This entity stores filter criteria for hasShotsLogged and shot count ranges.
 *
 * @property id Primary key, always 1 for the single active filter.
 * @property hasShotsLogged Filter for players with shots logged. null = no filter, true = has shots, false = no shots.
 * @property minShots Minimum number of shots for filtering. null = no minimum.
 * @property maxShots Maximum number of shots for filtering. null = no maximum.
 * @property lastUpdated Timestamp for when the filter was last updated.
 */
@Entity(tableName = "playerFilters")
data class PlayerFilterEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "hasShotsLogged")
    val hasShotsLogged: Boolean? = null,
    @ColumnInfo(name = "minShots")
    val minShots: Int? = null,
    @ColumnInfo(name = "maxShots")
    val maxShots: Int? = null,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: Long = System.currentTimeMillis()
)
