package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing a declared shot stored in the local database.
 *
 * @property id The unique identifier for the declared shot.
 * @property shotCategory The category of the shot (e.g., "other", "jump shot").
 * @property title The title or name of the shot.
 * @property description A brief description of the shot.
 * @property firebaseKey Optional Firebase key associated with this shot.
 */
@Entity(tableName = "declaredShots")
data class DeclaredShotEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "shotCategory")
    val shotCategory: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "firebaseKey")
    val firebaseKey: String? = null
)

/**
 * Converts a [DeclaredShotEntity] to its corresponding domain model [DeclaredShot].
 *
 * @return A [DeclaredShot] instance with values mapped from this entity.
 */
fun DeclaredShotEntity.toDeclaredShot(): DeclaredShot {
    return DeclaredShot(
        id = id,
        shotCategory = shotCategory,
        title = title,
        description = description,
        firebaseKey = firebaseKey
    )
}
