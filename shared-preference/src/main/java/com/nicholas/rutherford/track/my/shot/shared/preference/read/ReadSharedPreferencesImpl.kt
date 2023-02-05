package com.nicholas.rutherford.track.my.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants

class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    override fun accountHasBeenCreated(): Boolean {
        return sharedPreferences.getBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, false)
    }
}
