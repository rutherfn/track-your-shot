package com.nicholas.rutherford.track.my.shot.shared.preferences.create

import android.app.Application
import android.content.Context

class CreateSharedPreferencesImpl(application: Application) : CreateSharedPreferences {

    private val sharedPreference = application.getSharedPreferences(
        SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES,
        Context.MODE_PRIVATE
    )
    private val editor = sharedPreference.edit()

    override fun createAccountHasBeenCreatedPreference(value: Boolean) {
        editor.putBoolean(SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED, value)
        editor.apply()
    }


}