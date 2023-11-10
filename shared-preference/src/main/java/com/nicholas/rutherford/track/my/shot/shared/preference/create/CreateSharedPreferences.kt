package com.nicholas.rutherford.track.your.shot.shared.preference.create

interface CreateSharedPreferences {
    fun createAccountHasBeenCreatedPreference(value: Boolean)
    fun createUnverifiedEmailPreference(value: String)
    fun createUnverifiedUsernamePreference(value: String)
}
