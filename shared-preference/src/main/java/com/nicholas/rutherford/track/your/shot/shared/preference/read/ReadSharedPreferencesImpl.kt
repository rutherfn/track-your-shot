package com.nicholas.rutherford.track.your.shot.shared.preference.read

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class ReadSharedPreferencesImpl(private val sharedPreferences: SharedPreferences) : ReadSharedPreferences {

    override fun appHasBeenLaunched(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.APP_HAS_LAUNCHED, false)

    override fun shouldUpdateLoggedInPlayerListState(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST, false)

    override fun shouldUpdateLoggedInDeclaredShotListState(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST, false)

    override fun shouldShowTermsAndConditions(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS, false)

    override fun hasAccountBeenAuthenticated(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.HAS_AUTHENTICATED_ACCOUNT, false)

    override fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(Constants.Preferences.IS_LOGGED_IN, false)

    override fun playerFilterName(): String = sharedPreferences.getString(Constants.Preferences.PLAYER_FILTER_NAME, "") ?: ""

    override fun declaredShotId(): Int = sharedPreferences.getInt(Constants.Preferences.DECLARED_SHOT_ID, -1)
}
