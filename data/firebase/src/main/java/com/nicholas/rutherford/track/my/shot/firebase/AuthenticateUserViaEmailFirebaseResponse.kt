package com.nicholas.rutherford.track.your.shot.firebase

data class AuthenticateUserViaEmailFirebaseResponse(
    val isSuccessful: Boolean,
    val isAlreadyAuthenticated: Boolean,
    val isUserExist: Boolean
)
