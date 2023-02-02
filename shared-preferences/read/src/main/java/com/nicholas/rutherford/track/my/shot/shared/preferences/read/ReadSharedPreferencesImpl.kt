package com.nicholas.rutherford.track.my.shot.shared.preferences.read

import android.app.Application
import android.content.Context
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants

class ReadSharedPreferencesImpl(application: Application) : ReadSharedPreferences {

    private val sharedPreference = application.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, Context.MODE_PRIVATE)

    override fun accountHasBeenCreated(): Boolean {
        return sharedPreference.getBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, false)
    }
}
