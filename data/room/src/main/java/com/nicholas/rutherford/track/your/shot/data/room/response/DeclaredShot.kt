package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity

data class DeclaredShot (
    val id: Int,
    val value: String,
    val shotCategory: String,
    val title: String,
    val description: String
)

fun DeclaredShot.toDeclaredShotEntity(): DeclaredShotEntity {
    return DeclaredShotEntity(
        id = id,
        value = value,
        shotCategory = shotCategory,
        title = title,
        description = description
    )
}