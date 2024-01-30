package com.nicholas.rutherford.track.your.shot.shared.preference.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    override fun createAppHasLaunchedPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.APP_HAS_LAUNCHED, value)
        editor.apply()
    }

    override fun createHasLoggedInPlayerListPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.HAS_LOGGED_IN_PLAYER_LIST, value)
        editor.apply()
    }

    override fun createHasLoggedInDeclaredShotListPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.HAS_LOGGED_IN_DECLARED_SHOT_LIST, value)
        editor.apply()
    }
}
