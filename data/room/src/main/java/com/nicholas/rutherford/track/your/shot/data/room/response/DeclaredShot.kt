package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a declared basketball shot with details such as category, title, description,
 * and an optional Firebase key for remote storage or synchronization.
 *
 * @property id Unique identifier for the declared shot.
 * @property shotCategory Category of the shot (e.g., "Three Pointer", "Free Throw").
 * @property title The title or name of the shot.
 * @property description Detailed description of the shot.
 * @property firebaseKey Optional Firebase key corresponding to this shot for cloud storage.
 */
data class DeclaredShot(
    val id: Int,
    val shotCategory: String,
    val title: String,
    val description: String,
    val firebaseKey: String?
)

/**
 * Converts a [DeclaredShot] instance to a [DeclaredShotEntity] suitable for Room database storage.
 *
 * @return [DeclaredShotEntity] instance with the same property values.
 */
fun DeclaredShot.toDeclaredShotEntity(): DeclaredShotEntity {
    return DeclaredShotEntity(
        id = id,
        shotCategory = shotCategory,
        title = title,
        description = description,
        firebaseKey = firebaseKey
    )
}
