package com.nicholas.rutherford.track.my.shot.firebase

data class AuthenticateUserViaEmailFirebaseResponse(
    val isSuccessful: Boolean,
    val isAlreadyAuthenticated: Boolean,
    val isUserExist: Boolean
)
