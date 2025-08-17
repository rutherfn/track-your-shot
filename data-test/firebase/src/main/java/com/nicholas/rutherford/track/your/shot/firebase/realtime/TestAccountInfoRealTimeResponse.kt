package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [AccountInfoRealtimeResponse].
 * Provides predefined test data for the user name and email.
 */
class TestAccountInfoRealTimeResponse {

    /**
     * Creates a test [AccountInfoRealtimeResponse] instance with predefined test values.
     *
     * @return an [AccountInfoRealtimeResponse] containing test user name and email.
     */
    fun create(): AccountInfoRealtimeResponse {
        return AccountInfoRealtimeResponse(
            userName = USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE,
            email = EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE
        )
    }
}

/** Predefined test user name for [AccountInfoRealtimeResponse]. */
const val USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE = "boomyNicholasR"

/** Predefined test email for [AccountInfoRealtimeResponse]. */
const val EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE = "testEmail@yahoo.com"
