package com.nicholas.rutherford.track.your.shot.firebase.util.authentication

import com.nicholas.rutherford.track.your.shot.firebase.AuthenticateUserViaEmailFirebaseResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationFirebase {
    fun attemptToSendEmailVerificationForCurrentUser(): Flow<AuthenticateUserViaEmailFirebaseResponse>
    fun attemptToSendPasswordResetFlow(email: String): Flow<Boolean>
}
