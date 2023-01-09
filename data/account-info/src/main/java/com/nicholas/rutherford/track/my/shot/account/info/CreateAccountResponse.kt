package com.nicholas.rutherford.track.my.shot.account.info

data class CreateAccountResponse(
    val isSuccessful: Boolean = false,
    val username: String? = null,
    val isNewUser: Boolean? = null,
    val exception: Exception? = null
)