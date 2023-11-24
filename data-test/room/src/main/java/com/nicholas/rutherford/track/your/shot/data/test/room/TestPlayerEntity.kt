package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions

class TestPlayerEntity {

    fun create(): PlayerEntity {
        return PlayerEntity(
            id = PLAYER_ID,
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            position = position,
            firebaseKey = firebaseKey,
            imageUrl = IMAGE_URL
        )
    }
}

const val PLAYER_ID = 1
const val FIRST_NAME = "first"
const val LAST_NAME = "last"
val position = PlayerPositions.Center
const val firebaseKey = "firebaseKey"
const val IMAGE_URL = "url"
