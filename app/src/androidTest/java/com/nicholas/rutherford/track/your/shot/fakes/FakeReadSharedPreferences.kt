package com.nicholas.rutherford.track.your.shot.fakes

import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences

class FakeReadSharedPreferences(
    private val accountHasBeenCreated: Boolean = false,
    private val unverifiedEmail: String? = null,
    private val unverifiedUsername: String? = null
) : ReadSharedPreferences {
    override fun accountHasBeenCreated(): Boolean = accountHasBeenCreated

    override fun unverifiedEmail(): String? = unverifiedEmail

    override fun unverifiedUsername(): String? = unverifiedUsername
}
