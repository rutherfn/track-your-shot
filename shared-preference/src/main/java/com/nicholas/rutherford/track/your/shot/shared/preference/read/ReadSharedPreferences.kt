package com.nicholas.rutherford.track.your.shot.shared.preference.read

interface ReadSharedPreferences {

    fun appHasBeenLaunched(): Boolean
    fun hasLoggedInPlayerList(): Boolean
    fun hasLoggedInDeclaredShotList(): Boolean
}
