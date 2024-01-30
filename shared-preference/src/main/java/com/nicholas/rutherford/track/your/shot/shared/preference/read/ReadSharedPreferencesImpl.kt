package com.nicholas.rutherford.track.your.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants

class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    override fun appHasBeenLaunched(): Boolean {
        return sharedPreferences.getBoolean(SharedPreferencesConstants.Preferences.APP_HAS_LAUNCHED, false)
    }
}
