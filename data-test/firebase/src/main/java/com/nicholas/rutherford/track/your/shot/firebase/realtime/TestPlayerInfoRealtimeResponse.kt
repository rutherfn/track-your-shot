package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [PlayerInfoRealtimeResponse].
 * Provides predefined values for first name, last name, position, image URL,
 * and a list of test shot logs.
 */
class TestPlayerInfoRealtimeResponse {

    /**
     * Creates a test [PlayerInfoRealtimeResponse] instance with predefined
     * first name, last name, position value, image URL, and shot logs.
     *
     * @return a [PlayerInfoRealtimeResponse] populated with test values.
     */
    fun create(): PlayerInfoRealtimeResponse {
        return PlayerInfoRealtimeResponse(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            positionValue = POSITION_VALUE,
            imageUrl = IMAGE_URL,
            shotsLogged = listOf(TestShotLoggedRealtimeResponse.build())
        )
    }
}

/** Predefined test first name for [PlayerInfoRealtimeResponse]. */
const val FIRST_NAME = "firstName"

/** Predefined test last name for [PlayerInfoRealtimeResponse]. */
const val LAST_NAME = "lastName"

/** Predefined test position value for [PlayerInfoRealtimeResponse]. */
const val POSITION_VALUE = 2

/** Predefined test image URL for [PlayerInfoRealtimeResponse]. */
const val IMAGE_URL = "imageUrl"
