package com.nicholas.rutherford.track.my.shot.account.info

import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

data class CreateAccountResponse(
    val isSuccessful: Boolean = false,
    val additionalUserInfo: AdditionalUserInfo? = null,
    val authCredential: AuthCredential? = null,
    val firebaseUser: FirebaseUser? = null,
    val exception: Exception? = null
)