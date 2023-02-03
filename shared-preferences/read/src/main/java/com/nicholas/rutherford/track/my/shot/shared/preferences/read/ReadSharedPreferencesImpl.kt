package com.nicholas.rutherford.track.my.shot.shared.preferences.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants

class ReadSharedPreferencesImpl(private val sharedPreference: SharedPreferences) : ReadSharedPreferences {

    override fun accountHasBeenCreated(): Boolean {
        return sharedPreference.getBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, false)
    }
}
