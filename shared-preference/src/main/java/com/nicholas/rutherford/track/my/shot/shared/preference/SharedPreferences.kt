package com.nicholas.rutherford.track.my.shot.shared.preference

import android.app.Application
import android.content.SharedPreferences
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences

class SharedPreferences(application: Application) : CreateSharedPreferences, ReadSharedPreferences {

    private val sharedPreference: SharedPreferences = application.getSharedPreferences(
        SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE
    )
    private val editor = sharedPreference.edit()

    override fun createAccountHasBeenCreatedPreference(value: Boolean) {
        editor.putBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, value)
        editor.apply()
    }

    override fun accountHasBeenCreated(): Boolean {
        return sharedPreference.getBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, false)
    }
}
