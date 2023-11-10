package com.nicholas.rutherford.track.your.shot.shared.preference.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants

class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    override fun createAccountHasBeenCreatedPreference(value: Boolean) {
        editor.putBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, value)
        editor.apply()
    }

    override fun createUnverifiedEmailPreference(value: String) {
        editor.putString(SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL, value)
        editor.apply()
    }

    override fun createUnverifiedUsernamePreference(value: String) {
        editor.putString(SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME, value)
        editor.apply()
    }
}
