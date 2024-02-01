package com.nicholas.rutherford.track.your.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    override fun appHasBeenLaunched(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.APP_HAS_LAUNCHED, false)

    override fun shouldUpdateLoggedInPlayerListState(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST, false)

    override fun shouldUpdateLoggedInDeclaredShotListState(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST, false)
}
