package com.nicholas.rutherford.track.my.shot.firebase.realtime

class TestCreateAccountFirebaseRealtimeDatabaseResult {
    fun create(): CreateAccountFirebaseRealtimeDatabaseResult {
        return CreateAccountFirebaseRealtimeDatabaseResult(
            username = USER_NAME,
            email = EMAIL
        )
    }
}

const val USER_NAME = "boomyNicholasR"
const val EMAIL = "testEmail@yahoo.com"
