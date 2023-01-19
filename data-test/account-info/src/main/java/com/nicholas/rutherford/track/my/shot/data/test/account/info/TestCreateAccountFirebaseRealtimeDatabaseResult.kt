package com.nicholas.rutherford.track.my.shot.data.test.account.info

import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseRealtimeDatabaseResult

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
