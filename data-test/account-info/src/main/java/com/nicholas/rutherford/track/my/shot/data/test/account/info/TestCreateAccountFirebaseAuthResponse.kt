package com.nicholas.rutherford.track.my.shot.data.test.account.info

import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseAuthResponse

class TestCreateAccountFirebaseAuthResponse {
    fun create(): CreateAccountFirebaseAuthResponse {
        return CreateAccountFirebaseAuthResponse(
            isSuccessful = IS_SUCCESSFUL,
            username = USERNAME,
            isNewUser = IS_NEW_USER,
            exception = EXCEPTION
        )
    }
}

const val IS_SUCCESSFUL = true
const val USERNAME = "boomyNicholasR"
const val IS_NEW_USER = true
val EXCEPTION = Exception("Something went wrong")