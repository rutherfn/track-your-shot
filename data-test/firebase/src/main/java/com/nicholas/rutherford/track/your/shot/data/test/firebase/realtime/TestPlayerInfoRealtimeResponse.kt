package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse].
 * Provides predefined values for first name, last name, position, image URL,
 * and a list of test shot logs.
 */
class TestPlayerInfoRealtimeResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse] instance with predefined
     * first name, last name, position value, image URL, and shot logs.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse] populated with test values.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            positionValue = POSITION_VALUE,
            imageUrl = IMAGE_URL,
            shotsLogged = listOf(TestShotLoggedRealtimeResponse.build())
        )
    }
}

/** Predefined test first name for [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse]. */
const val FIRST_NAME = "firstName"

/** Predefined test last name for [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse]. */
const val LAST_NAME = "lastName"

/** Predefined test position value for [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse]. */
const val POSITION_VALUE = 2

/** Predefined test image URL for [com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse]. */
const val IMAGE_URL = "imageUrl"
