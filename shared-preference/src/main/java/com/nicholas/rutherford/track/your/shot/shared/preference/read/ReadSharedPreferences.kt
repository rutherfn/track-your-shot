package com.nicholas.rutherford.track.your.shot.shared.preference.read

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Defines a default interface for reading from [android.content.SharedPreferences].
 */
interface ReadSharedPreferences {
    fun appHasBeenLaunched(): Boolean
    fun shouldShowTermsAndConditions(): Boolean
    fun isLoggedIn(): Boolean
    fun playerFilterName(): String
}
