package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

data class ShotIgnoring(
    val id: Int,
    val shotId: Int
)

fun ShotIgnoring.toShotIgnoringEntity(): ShotIgnoringEntity {
    return ShotIgnoringEntity(id = id, shotId = shotId)
}
