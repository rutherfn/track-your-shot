package com.nicholas.rutherford.track.your.shot.shared.preference.read

interface ReadSharedPreferences {

    fun accountHasBeenCreated(): Boolean

    fun unverifiedEmail(): String?
    fun unverifiedUsername(): String?
}
