package com.nicholas.rutherford.track.my.shot.data.test.account.info

import com.nicholas.rutherford.track.my.shot.account.info.AuthenticateUserViaEmailFirebaseResponse

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
