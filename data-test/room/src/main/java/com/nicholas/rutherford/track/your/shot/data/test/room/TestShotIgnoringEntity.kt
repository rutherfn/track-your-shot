package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

object TestShotIgnoringEntity {

    fun build(): ShotIgnoringEntity {
        return ShotIgnoringEntity(
            id = SHOT_IGNORING_ID,
            shotId = SHOT_IGNORING_SHOT_ID
        )
    }
}

const val SHOT_IGNORING_ID = 1
const val SHOT_IGNORING_SHOT_ID = 22
