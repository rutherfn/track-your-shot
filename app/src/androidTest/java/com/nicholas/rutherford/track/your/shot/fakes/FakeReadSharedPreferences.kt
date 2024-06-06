package com.nicholas.rutherford.track.your.shot.fakes

import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences

class FakeReadSharedPreferences(
    private val appHasBeenLaunched: Boolean = false,
    private val shouldUpdateLoggedInListState: Boolean = false,
    private val shouldUpdateLoggedInDeclaredShotListState: Boolean = false
) : ReadSharedPreferences {
    override fun appHasBeenLaunched(): Boolean = appHasBeenLaunched

    override fun shouldUpdateLoggedInPlayerListState(): Boolean = shouldUpdateLoggedInListState

    override fun shouldUpdateLoggedInDeclaredShotListState(): Boolean = shouldUpdateLoggedInDeclaredShotListState
}
