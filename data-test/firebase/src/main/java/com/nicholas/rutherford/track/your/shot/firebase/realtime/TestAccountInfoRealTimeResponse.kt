package com.nicholas.rutherford.track.your.shot.firebase.realtime

class TestAccountInfoRealTimeResponse {

    fun create(): AccountInfoRealtimeResponse {
        return AccountInfoRealtimeResponse(
            userName = USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE,
            email = EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE,
            defaultShotIdsToIgnore = DEFAULT_SHOT_IDS_TO_IGNORE
        )
    }
}

const val USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE = "boomyNicholasR"
const val EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE = "testEmail@yahoo.com"
val DEFAULT_SHOT_IDS_TO_IGNORE = listOf(1, 2, 4)
