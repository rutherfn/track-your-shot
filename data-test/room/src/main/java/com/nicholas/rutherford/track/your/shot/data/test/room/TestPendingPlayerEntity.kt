package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions

object TestPendingPlayerEntity {

    fun build(): PendingPlayerEntity {
        return PendingPlayerEntity(
            id = PENDING_PLAYER_ID,
            firstName = PENDING_FIRST_NAME,
            lastName = PENDING_LAST_NAME,
            position = position,
            firebaseKey = PENDING_FIREBASE_KEY,
            imageUrl = PENDING_IMAGE_URL,
            shotsLoggedList = listOf(TestShotLogged.build())
        )
    }

    private const val PENDING_PLAYER_ID = 1
    private const val PENDING_FIRST_NAME = "first"
    private const val PENDING_LAST_NAME = "last"
    private val position = PlayerPositions.Center
    private const val PENDING_FIREBASE_KEY = "firebaseKey"
    private const val PENDING_IMAGE_URL = "url"
}
