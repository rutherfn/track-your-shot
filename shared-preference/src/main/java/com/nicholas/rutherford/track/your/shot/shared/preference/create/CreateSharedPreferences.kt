package com.nicholas.rutherford.track.your.shot.shared.preference.create

interface CreateSharedPreferences {
    fun createAppHasLaunchedPreference(value: Boolean)
    fun createHasLoggedInPlayerListPreference(value: Boolean)
    fun createHasLoggedInDeclaredShotListPreference(value: Boolean)
}
