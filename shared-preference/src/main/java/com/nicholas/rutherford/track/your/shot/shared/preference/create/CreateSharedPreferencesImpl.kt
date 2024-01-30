package com.nicholas.rutherford.track.your.shot.shared.preference.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants

class CreateSharedPreferencesImpl(private val editor: SharedPreferences.Editor) : CreateSharedPreferences {

    override fun createAppHasLaunchedPreference(value: Boolean) {
        editor.putBoolean(SharedPreferencesConstants.Preferences.APP_HAS_LAUNCHED, value)
        editor.apply()
    }
}
