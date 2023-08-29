package com.nicholas.rutherford.track.my.shot.firebase.util.authentication

import com.nicholas.rutherford.track.my.shot.data.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationFirebase {
    fun attemptToSendEmailVerificationForCurrentUser(): Flow<com.nicholas.rutherford.track.my.shot.data.firebase.AuthenticateUserViaEmailFirebaseResponse>
    fun attemptToSendPasswordResetFlow(email: String): Flow<Boolean>
}
