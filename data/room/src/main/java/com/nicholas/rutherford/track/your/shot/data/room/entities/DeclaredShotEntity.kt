package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

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

fun DeclaredShotEntity.toDeclaredShot(): DeclaredShot {
    return DeclaredShot(
        id = id,
        shotCategory = shotCategory,
        title = title,
        description = description,
        firebaseKey = firebaseKey
    )
}
