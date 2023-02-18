package com.nicholas.rutherford.track.my.shot.shared.preference.read

interface ReadSharedPreferences {

    fun accountHasBeenCreated(): Boolean

    fun unverifiedEmail(): String?
    fun unverifiedUsername(): String?
}
