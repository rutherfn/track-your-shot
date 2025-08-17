package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse].
 * Provides predefined test data for the user name and email.
 */
class TestAccountInfoRealTimeResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse] instance with predefined test values.
     *
     * @return an [com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse] containing test user name and email.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse(
            userName = USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE,
            email = EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE
        )
    }
}

/** Predefined test user name for [com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse]. */
const val USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE = "boomyNicholasR"

/** Predefined test email for [com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse]. */
const val EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE = "testEmail@yahoo.com"
