package com.nicholas.rutherford.track.my.shot.firebase

data class CreateAccountFirebaseAuthResponse(
    val isSuccessful: Boolean = false,
    val username: String? = null,
    val isNewUser: Boolean? = null,
    val exception: Exception? = null
)