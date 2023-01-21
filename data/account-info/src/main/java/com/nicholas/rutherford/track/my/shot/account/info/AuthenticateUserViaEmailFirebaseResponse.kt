package com.nicholas.rutherford.track.my.shot.account.info

data class AuthenticateUserViaEmailFirebaseResponse (
    val isSuccessful: Boolean,
    val isAlreadyAuthenticated: Boolean,
    val isUserExist: Boolean
    )