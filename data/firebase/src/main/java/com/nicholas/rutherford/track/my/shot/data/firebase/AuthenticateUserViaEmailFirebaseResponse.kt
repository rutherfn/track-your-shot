package com.nicholas.rutherford.track.my.shot.data.firebase

data class AuthenticateUserViaEmailFirebaseResponse(
    val isSuccessful: Boolean,
    val isAlreadyAuthenticated: Boolean,
    val isUserExist: Boolean
)
