package com.nicholas.rutherford.track.your.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Implementation of [ReadSharedPreferences] responsible for reading from [SharedPreferences].
 * This class reads various user-related flags and data into persistent storage.
 * todo -> Want to switch over to DataStore in the future
 *
 * @param sharedPreferences The [SharedPreferences] instance to read from.
 */
class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    /** Reads whether the app has been launched before. */
    override fun appHasBeenLaunched(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.APP_HAS_LAUNCHED, false)

    /** Reads whether the terms and conditions should be shown. */
    override fun shouldShowTermsAndConditions(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS, false)

    /** Reads whether the user is logged in. */
    override fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.IS_LOGGED_IN, false)

    /** Reads user player filtered by name. */
    override fun playerFilterName(): String = sharedPreferences.getString(Constants.Preferences.PLAYER_FILTER_NAME, "") ?: ""
}
