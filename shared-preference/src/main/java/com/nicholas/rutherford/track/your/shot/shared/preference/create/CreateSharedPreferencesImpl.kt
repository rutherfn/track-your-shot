package com.nicholas.rutherford.track.your.shot.shared.preference.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    override fun createAppHasLaunchedPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.APP_HAS_LAUNCHED, value)
        editor.apply()
    }

    override fun createShouldUpdateLoggedInPlayerListPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST, value)
        editor.apply()
    }

    override fun createShouldUpdateLoggedInDeclaredShotListPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST, value)
        editor.apply()
    }

    override fun createShouldShowTermsAndConditionsPreference(value: Boolean) {
        editor.putBoolean(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS, value)
        editor.apply()
    }

    override fun createHasAuthenticatedAccount(value: Boolean) {
        editor.putBoolean(Constants.Preferences.HAS_AUTHENTICATED_ACCOUNT, value)
        editor.apply()
    }

    override fun createIsLoggedIn(value: Boolean) {
        editor.putBoolean(Constants.Preferences.IS_LOGGED_IN, value)
        editor.apply()
    }
}
