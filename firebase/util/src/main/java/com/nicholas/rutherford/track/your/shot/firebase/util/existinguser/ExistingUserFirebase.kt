package com.nicholas.rutherford.track.your.shot.firebase.util.existinguser

import kotlinx.coroutines.flow.Flow

interface ExistingUserFirebase {
    fun logout()
    fun loginFlow(email: String, password: String): Flow<Boolean>
}
