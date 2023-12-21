package com.nicholas.rutherford.track.your.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants

class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    override fun appHasBeenLaunched(): Boolean {
        return sharedPreferences.getBoolean(SharedPreferencesConstants.Preferences.APP_HAS_LAUNCHED, false)
    }

    override fun accountHasBeenCreated(): Boolean {
        return sharedPreferences.getBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, false)
    }

    override fun unverifiedEmail(): String? {
        val unverifiedEmail = sharedPreferences.getString(SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL, null)

        return if (unverifiedEmail?.isEmpty() == true) {
            return null
        } else {
            unverifiedEmail
        }
    }

    override fun unverifiedUsername(): String? {
        val unverifiedUsername = sharedPreferences.getString(SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME, null)

        return if (unverifiedUsername?.isEmpty() == true) {
            null
        } else {
            unverifiedUsername
        }
    }
}
