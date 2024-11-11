package com.nicholas.rutherford.track.your.shot.shared.preference.create

interface CreateSharedPreferences {
    fun createAppHasLaunchedPreference(value: Boolean)
    fun createShouldUpdateLoggedInPlayerListPreference(value: Boolean)
    fun createShouldUpdateLoggedInDeclaredShotListPreference(value: Boolean)
    fun createShouldShowTermsAndConditionsPreference(value: Boolean)
}
