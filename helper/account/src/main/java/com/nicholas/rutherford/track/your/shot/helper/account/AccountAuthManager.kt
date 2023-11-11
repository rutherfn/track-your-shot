package com.nicholas.rutherford.track.your.shot.helper.account

interface AccountAuthManager {
    fun logout()
    fun login(email: String, password: String)
}
