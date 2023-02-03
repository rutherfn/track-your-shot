package com.nicholas.rutherford.track.my.shot.shared.preferences.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants

class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    override fun createAccountHasBeenCreatedPreference(value: Boolean) {
        editor.putBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, value)
        editor.apply()
    }
}
