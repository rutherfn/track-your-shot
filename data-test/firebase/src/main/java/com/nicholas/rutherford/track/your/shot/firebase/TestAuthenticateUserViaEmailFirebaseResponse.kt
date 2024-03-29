package com.nicholas.rutherford.track.your.shot.firebase

class TestAuthenticateUserViaEmailFirebaseResponse {
    fun create(): AuthenticateUserViaEmailFirebaseResponse {
        return AuthenticateUserViaEmailFirebaseResponse(
            isSuccessful = IS_SUCCESSFUL_AUTHENTICATED,
            isAlreadyAuthenticated = IS_ALREADY_AUTHENTICATED,
            isUserExist = IS_USER_EXIST
        )
    }
}

const val IS_SUCCESSFUL_AUTHENTICATED = true
const val IS_ALREADY_AUTHENTICATED = false
const val IS_USER_EXIST = false
