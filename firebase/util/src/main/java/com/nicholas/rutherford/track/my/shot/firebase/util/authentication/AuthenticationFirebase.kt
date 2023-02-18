package com.nicholas.rutherford.track.my.shot.firebase.util.authentication

import com.nicholas.rutherford.track.my.shot.account.info.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationFirebase {
    fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse>
}
