package com.nicholas.rutherford.track.your.shot.shared.preference.read

interface ReadSharedPreferences {

    fun appHasBeenLaunched(): Boolean
    fun shouldUpdateLoggedInPlayerListState(): Boolean
    fun shouldUpdateLoggedInDeclaredShotListState(): Boolean
    fun shouldShowTermsAndConditions(): Boolean
    fun hasAccountBeenAuthenticated(): Boolean
}
