package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring

@Entity(tableName = "shotsIgnoring")
data class ShotIgnoringEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "shotId")
    val shotId: Int
)

fun ShotIgnoringEntity.toShotIgnoring(): ShotIgnoring {
    return ShotIgnoring(id = id, shotId = shotId)
}
