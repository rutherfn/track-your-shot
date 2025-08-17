package com.nicholas.rutherford.track.your.shot.shared.preference.create

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Defines a default interface for creating and storing from [android.content.SharedPreferences].
 */
interface CreateSharedPreferences {
    fun createAppHasLaunchedPreference(value: Boolean)
    fun createShouldShowTermsAndConditionsPreference(value: Boolean)
    fun createIsLoggedIn(value: Boolean)
    fun createPlayerFilterName(value: String)
}
