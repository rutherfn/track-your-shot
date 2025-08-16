package com.nicholas.rutherford.track.your.shot.shared.preference.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Implementation of [CreateSharedPreferences] responsible for saving values to [SharedPreferences].
 * This class writes various user-related flags and data into persistent storage.
 * todo -> Want to switch over to DataStore in the future
 *
 * @param editor The [SharedPreferences.Editor] used to save preference values.
 */
class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    /** Saves whether the app has been launched before. */
    override fun createAppHasLaunchedPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.APP_HAS_LAUNCHED, value)
        editor.apply()
    }

    /** Saves whether the terms and conditions should be shown to the user. */
    override fun createShouldShowTermsAndConditionsPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS, value)
        editor.apply()
    }

    /** Saves the logged-in state of the user. */
    override fun createIsLoggedIn(value: Boolean) {
        editor.putBoolean(Constants.Preferences.IS_LOGGED_IN, value)
        editor.apply()
    }

    /** Saves the player filter name. */
    override fun createPlayerFilterName(value: String) {
        editor.putString(Constants.Preferences.PLAYER_FILTER_NAME, value)
        editor.apply()
    }
}
