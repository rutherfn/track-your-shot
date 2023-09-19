package com.nicholas.rutherford.track.my.shot.firebase

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
