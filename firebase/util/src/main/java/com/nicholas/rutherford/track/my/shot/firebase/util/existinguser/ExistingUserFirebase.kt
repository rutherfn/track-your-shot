package com.nicholas.rutherford.track.my.shot.firebase.util.existinguser

import kotlinx.coroutines.flow.Flow

interface ExistingUserFirebase {
    fun logOut()
    fun logInFlow(email: String, password: String): Flow<Boolean>
}
