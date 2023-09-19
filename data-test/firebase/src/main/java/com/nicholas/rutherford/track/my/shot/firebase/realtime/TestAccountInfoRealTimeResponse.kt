package com.nicholas.rutherford.track.my.shot.firebase.realtime

class TestAccountInfoRealTimeResponse {

    fun create(): AccountInfoRealtimeResponse {
        return AccountInfoRealtimeResponse(
            userName = USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE,
            email = EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE
        )
    }
}

const val USER_NAME_ACCOUNT_INFO_REALTIME_RESPONSE = "boomyNicholasR"
const val EMAIL_ACCOUNT_INFO_REALTIME_RESPONSE = "testEmail@yahoo.com"
